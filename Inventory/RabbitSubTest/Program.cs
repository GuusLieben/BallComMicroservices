using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;

namespace RabbitSubTest
{
	class Program
	{
		static void Main(string[] args)
		{
			ConnectionFactory factory = new ConnectionFactory() { HostName = "localhost" };

			using IConnection connection = factory.CreateConnection();
			using IModel channel = connection.CreateModel();

			channel.ExchangeDeclare("BallExchange", ExchangeType.Fanout);
			channel.QueueDeclare("Inventory", true, false, false);
			channel.QueueBind("Inventory", "BallExchange", "", null);

			EventingBasicConsumer consumer = new EventingBasicConsumer(channel);
			consumer.Received += (model, ea) =>
			{
				
				byte[] body = ea.Body.ToArray();
				string message = Encoding.UTF8.GetString(body);
				Console.WriteLine(" [x] {0}", message);
				Console.WriteLine(ea.BasicProperties.Headers.ToString());
			};


			channel.BasicConsume("Inventory", true, consumer);

			Console.WriteLine(" Press [enter] to exit.");
			Console.ReadLine();
		}
	}
}
