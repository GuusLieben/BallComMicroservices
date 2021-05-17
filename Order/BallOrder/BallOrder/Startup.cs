using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using BallOrderInfrastructure.RabbitMQ;
using BallOrderInfrastructure.Repositories;
using Microsoft.EntityFrameworkCore;

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
            services.AddDbContext<BallOrderDBContext>(options =>
            {
                options.UseSqlServer(Configuration["ball-order-db-local:ConnectionString"], actions => actions.MigrationsAssembly("BallOrderInfrastructure"));
            });

            services.AddScoped<IOrderProductRepository, EFOrderProductRepository>();
            services.AddScoped<IOrderRepository, EFOrderRepository>();

            services.UseRabbitMQMessageHandler(Configuration);

            services.AddControllers();
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
            scope.ServiceProvider.GetService<BallOrderDBContext>().MigrateDB();
        }
    }
}
