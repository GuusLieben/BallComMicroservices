using PaymentInfrastructure.Interfaces;
using System;
using Microsoft.Extensions.DependencyInjection;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using PaymentDomain.Services;
using PaymentDomain.Events;
using Newtonsoft.Json.Linq;
using PaymentDomain.Models;
using Microsoft.Extensions.Hosting;

namespace PaymentInfrastructure.RabbitMQ
{
    class RabbitMQMessageManager : IMessageHandler, IHostedService
    {
        private readonly IMessageListener _messageListener;
        private readonly IPaymentRepository _paymentRepository;
        private readonly IServiceScope _scope;

        public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
        {
            _scope = scopeFactory.CreateScope();
            _messageListener = _scope.ServiceProvider.GetRequiredService<IMessageListener>();
            _paymentRepository = _scope.ServiceProvider.GetRequiredService<IPaymentRepository>();
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _messageListener.Start(this);
            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            _messageListener.Stop();
            _scope.Dispose();

            return Task.CompletedTask;
        }


		public async Task<bool> HandleMessageAsync(string messageType, string message)
		{ 
			try
			{
				JObject messageObject = MessageSerializer.Deserialize(message);
				switch (messageType)
				{
					case "OrderCreated":
						await HandleAsync(messageObject.ToObject<OrderCreated>());
						break;
					default:
						break;
				}
			}
			catch (Exception)
			{
				return false;
			}

			return true;
		}

		private Task HandleAsync(OrderCreated evt)
		{
			Debug.WriteLine("-------------------------------------------");
            Debug.WriteLine("OrderId: " + evt.OrderId);
            Debug.WriteLine("TotalAmount: " + evt.TotalAmount);
            Debug.WriteLine("PaymentType: " + evt.PaymentType);

			Debug.WriteLine("-------------------------------------------");

			Debug.WriteLine("Handled event");

			Payment newPayment = new Payment()
			{
				PaymentId = Guid.NewGuid(),
				OrderId = evt.OrderId,
				PaymentType = evt.PaymentType,
				Amount = evt.TotalAmount,
				PaymentState = "Registered",
				CreationDate = new DateTime(),
				PaymentRecievedDate = new DateTime(1990, 1, 1)
            };

			_paymentRepository.Save(newPayment);

			//TODO publish PaymentRegistered Event

			return Task.CompletedTask;
		}
	}
}
