using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using BallOrderInfrastructure.RabbitMQ;
using BallOrderInfrastructure.Repositories;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Models;

namespace BallOrder
{
    public class Startup
    {
        public IConfiguration Configuration { get; }
        public IWebHostEnvironment Env { get; }

        public Startup(IConfiguration configuration, IWebHostEnvironment env)
        {
            Configuration = configuration;
            Env = env;
        }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddDbContext<WriteOrderDbContext>(options =>
            {
                options.UseSqlServer(Configuration["ball-order-db-write:ConnectionString"], actions => actions.MigrationsAssembly("BallOrderInfrastructure"));
            });

            services.AddDbContext<ReadOrderDbContext>(options =>
            {
                options.UseSqlServer(Configuration["ball-order-db-read:ConnectionString"], actions => actions.MigrationsAssembly("BallOrderInfrastructure"));
            });

            services.AddScoped<IOrderReadProductRepository, EFOrderReadProductRepository>();
            services.AddScoped<IOrderWriteRepository, EFOrderWriteRepository>();
            services.AddScoped<IOrderReadOrderRepository, EFOrderReadOrderRepository>();
            services.AddScoped<IOrderReadOrderProductRepository, EFOrderReadOrderProductRepository>();

            services.AddScoped<OrderEventReplayer>();

            services.UseRabbitMQMessageHandler(Configuration);
            services.UseRabbitMQMessagePublisher(Configuration);

            services.AddControllers()
                .AddNewtonsoftJson(options =>
                {
                    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
                });
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "BallOrder", Version = "v1" });
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app)
        {
            if (Env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "BallOrder v1"));
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

            using IServiceScope scope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope();
            scope.ServiceProvider.GetService<WriteOrderDbContext>().MigrateDB();
            scope.ServiceProvider.GetService<ReadOrderDbContext>().MigrateDB();
        }
    }
}
