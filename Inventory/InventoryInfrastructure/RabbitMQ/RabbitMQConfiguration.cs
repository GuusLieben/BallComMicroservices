using InventoryInfrastructure.RabbitMQ;
using InventoryInfrastructure.RabbitMQ.Interfaces;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace InventoryInfrastructure.RabbitMQ
{
	public static class RabbitMQConfiguration
	{
		private static string _host;
		private static string _username;
		private static string _password;
		private static string _exchange;
		private static string _queue;
		private static int _port;

		public static void UseRabbitMQMessageHandler(this IServiceCollection services, IConfiguration config)
		{
			ReadRabbitMQConfiguration(config, "RabbitMQHandler");

			// Register handler so it can be injected into manager
			services.AddTransient<IMessageListener>(_ => new RabbitMQMessageListener(_host, _port, _username, _password, _exchange, _queue));
			services.AddHostedService<RabbitMQMessageManager>();
		}

		private static void ReadRabbitMQConfiguration(IConfiguration config, string sectionName)
		{
			IConfigurationSection section = config.GetSection(sectionName);

			_host = section["Host"];
			_port = int.Parse(section["Port"]);
			_username = section["Username"];
			_password = section["Password"];
			_exchange = section["Exchange"];

			if (sectionName == "RabbitMQHandler")
				_queue = section["Queue"];
		}
	}
}
