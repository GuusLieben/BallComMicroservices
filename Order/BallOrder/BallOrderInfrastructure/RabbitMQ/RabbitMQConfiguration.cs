using BallOrderInfrastructure.RabbitMQ.Interfaces;
using Infrastructure.RabbitMQ;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace BallOrderInfrastructure.RabbitMQ
{
    public static class RabbitMQConfiguration
    {
        private static string _host;
        private static string _username;
        private static string _password;
        private static string _exchange;
        private static string _queue;

        public static void UseRabbitMQMessageHandler(this IServiceCollection services, IConfiguration config)
        {
            ReadRabbitMQConfiguration(config, "RabbitMQManager");

            // Register handler so it can be injected into manager
            services.AddTransient<IMessageListener>(_ => new RabbitMQMessageListener(_host, _username, _password, _exchange, _queue));
            services.AddHostedService<RabbitMQMessageManager>();
        }

        public static void UseRabbitMQMessagePublisher(this IServiceCollection services, IConfiguration config)
        {
            ReadRabbitMQConfiguration(config, "RabbitMQPublisher");
            services.AddTransient<IMessagePublisher>(_ => new RabbitMQMessagePublisher(_host, _username, _password, _exchange));
        }

        private static void ReadRabbitMQConfiguration(IConfiguration config, string sectionName)
        {
            IConfigurationSection section = config.GetSection(sectionName);

            _host = section["Host"];
            _username = section["Username"];
            _password = section["Password"];
            _exchange = section["Exchange"];

            if (sectionName == "RabbitMQManager")
                _queue = section["Queue"];
        }
    }
}