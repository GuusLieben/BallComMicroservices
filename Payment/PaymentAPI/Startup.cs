using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using PaymentInfrastructure.Context;
using PaymentDomain.Services;
using PaymentInfrastructure.Repositories;
using PaymentInfrastructure.RabbitMQ;

namespace PaymentAPI
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();

            services.AddDbContext<PaymentDbContext>(options =>
            {
                options.UseSqlServer(Configuration["Database:Main"], actions => actions.MigrationsAssembly("PaymentInfrastructure"));
            });

            services.AddDbContext<EventLogContext>(options =>
            {
                options.UseSqlServer(Configuration["Database:Main"], actions => actions.MigrationsAssembly("PaymentInfrastructure"));
            });

            services.AddScoped<IPaymentRepository, EFPaymentRepository>();

            services.UseRabbitMQMessageHandler(Configuration);
            services.UseRabbitMQMessagePublisher(Configuration);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

            // auto migrate db
            using IServiceScope scope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope();
            scope.ServiceProvider.GetService<PaymentDbContext>().MigrateDB();
            scope.ServiceProvider.GetService<EventLogContext>().MigrateDB();
        }
    }
}
