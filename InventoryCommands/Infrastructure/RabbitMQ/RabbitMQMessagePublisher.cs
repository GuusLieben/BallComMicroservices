using Domain.Events;
using Infrastructure.RabbitMQ.Interfaces;
using Polly;
using RabbitMQ.Client;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Infrastructure.RabbitMQ
{
	public class RabbitMQMessagePublisher : IMessagePublisher
	{
		private readonly string _host;
		private readonly string _username;
		private readonly string _password;
		private readonly string _exchange;

		private IConnection _connection;
		private IModel _model;

		public RabbitMQMessagePublisher(string host, string username, string password, string exchange)
		{
			_host = host;
			_username = username;
			_password = password;
			_exchange = exchange;

			Connect();
		}

		public Task PublishMessageAsync(IEvent e)
		{
			return Task.Run(() =>
			{
				string data = MessageSerializer.Serialize(e);
				byte[] body = Encoding.UTF8.GetBytes(data);

				IBasicProperties properties = _model.CreateBasicProperties();
				properties.Headers = new Dictionary<string, object> { { "MessageType", e.EventName } };

				_model.BasicPublish(_exchange, "", properties, body);
			});
		}

		private void Connect()
		{
			Policy
				.Handle<Exception>()
				.WaitAndRetry(9, r => TimeSpan.FromSeconds(5))
				.Execute(() =>
				{
					ConnectionFactory factory = new ConnectionFactory() { HostName = _host, UserName = _username, Password = _password };
					factory.AutomaticRecoveryEnabled = true;
					_connection = factory.CreateConnection();
					_model = _connection.CreateModel();
					_model.ExchangeDeclare(_exchange, "fanout", durable: true, autoDelete: false);
				});
		}
	}
}
