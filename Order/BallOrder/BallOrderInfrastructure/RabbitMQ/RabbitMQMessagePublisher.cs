using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using BallOrderInfrastructure.RabbitMQ.Interfaces;
using Polly;
using RabbitMQ.Client;

namespace BallOrderInfrastructure.RabbitMQ
{
    public class RabbitMQMessagePublisher : IMessagePublisher
    {
        private readonly string _host;
        private readonly int _port;
        private readonly string _username;
        private readonly string _password;
        private readonly string _exchange;
        private readonly string _queue;

        private IMessageManager _callback;
        private IModel _model;
        private IConnection _connection;

        public RabbitMQMessagePublisher(string host, int port, string username, string password, string exchange, string queue)
        {
            _host = host;
            _port = port;
            _username = username;
            _password = password;
            _exchange = exchange;
            _queue = queue;

            Policy
                .Handle<Exception>()
                .WaitAndRetry(9, r => TimeSpan.FromSeconds(5), (ex, ts) => { Console.WriteLine("Error connecting to RabbitMQ. Retrying in 5 sec."); })
                .Execute(() =>
                {
                    var factory = new ConnectionFactory() { HostName = _host, UserName = _username, Password = _password, Port = _port };
                    factory.AutomaticRecoveryEnabled = true;
                    _connection = factory.CreateConnection();
                    _model = _connection.CreateModel();
                    _model.ExchangeDeclare(_exchange, "fanout", durable: true, autoDelete: false);
                });
        }

        public Task PublishMessageAsync(string messageType, object message, string routingKey)
        {

            return Task.Run(() =>
            {
                string data = MessageSerializer.Serialize(message);
                var body = Encoding.UTF8.GetBytes(data);
                IBasicProperties properties = _model.CreateBasicProperties();
                properties.Headers = new Dictionary<string, object> { { "MessageType", messageType } };
                _model.BasicPublish(_exchange, routingKey, properties, body);
            });
        }
    }
}