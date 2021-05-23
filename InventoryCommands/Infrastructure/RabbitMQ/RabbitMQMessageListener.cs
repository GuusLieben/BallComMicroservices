using Infrastructure.RabbitMQ.Interfaces;
using Polly;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;
using System.Threading.Tasks;

namespace Infrastructure.RabbitMQ
{
	public class RabbitMQMessageListener : IMessageListener
	{
		private readonly string _host;
		private readonly string _username;
		private readonly string _password;
		private readonly string _exchange;
		private readonly string _queue;

		private IMessageHandler _callback;
		private AsyncEventingBasicConsumer _consumer;
		private IModel _model;
		private IConnection _connection;
		private string _consumerTag;

		public RabbitMQMessageListener(string host, string username, string password, string exchange, string queue)
		{
			_host = host;
			_username = username;
			_password = password;
			_exchange = exchange;
			_queue = queue;
		}

		public void Start(IMessageHandler callback)
		{
			_callback = callback;

			Policy
				.Handle<Exception>()
				.WaitAndRetry(9, r => TimeSpan.FromSeconds(5))
				.Execute(() =>
				{
					ConnectionFactory factory = new ConnectionFactory() { HostName = _host, UserName = _username, Password = _password, DispatchConsumersAsync = true };

					_connection = factory.CreateConnection();
					_model = _connection.CreateModel();

					_model.ExchangeDeclare(_exchange, "fanout", true, false);
					_model.QueueDeclare(_queue, durable: true, autoDelete: false, exclusive: false);
					_model.QueueBind(_queue, _exchange, "");

					_consumer = new AsyncEventingBasicConsumer(_model);
					_consumer.Received += async (sender, ea) =>
					{
						if (await HandleEvent(ea))
						{
							_model.BasicAck(ea.DeliveryTag, false);
						}
					};

					_consumerTag = _model.BasicConsume(_queue, false, _consumer);
				});
		}

		public void Stop()
		{
			_model.BasicCancel(_consumerTag);
			_model.Close(200, "Goodbye");
			_connection.Close();
		}

		private Task<bool> HandleEvent(BasicDeliverEventArgs ea)
		{
			string messageType = Encoding.UTF8.GetString((byte[])ea.BasicProperties.Headers["MessageType"]);
			string body = Encoding.UTF8.GetString(ea.Body.ToArray());

			return _callback.HandleMessageAsync(messageType, body);
		}
	}
}
