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
using PaymentInfrastructure.Context;

namespace PaymentInfrastructure.RabbitMQ
{
    class RabbitMQMessageManager : IMessageHandler, IHostedService
    {
        private readonly IMessageListener _messageListener;
        private readonly IPaymentRepository _paymentRepository;
		private readonly EventLogContext eventLogDb;
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
						await HandleAsync(messageObject.ToObject<OrderCreatedEvent>());
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

		private Task HandleAsync(OrderCreatedEvent evt)
		{
			Payment newPayment = new Payment()
			{
				PaymentId = Guid.NewGuid(),
				OrderId = evt.orderId,
				PaymentType = evt.paymentType,
				Amount = evt.totalPrice,
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
