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
        private readonly IServiceScope _scope;
		private readonly IMessagePublisher _messagePublisher;

        public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
        {
            _scope = scopeFactory.CreateScope();
            _messageListener = _scope.ServiceProvider.GetRequiredService<IMessageListener>();
            _paymentRepository = _scope.ServiceProvider.GetRequiredService<IPaymentRepository>();
			_messagePublisher = _scope.ServiceProvider.GetRequiredService<IMessagePublisher>();
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

		private async Task HandleAsync(OrderCreatedEvent evt)
		{
			Console.WriteLine("Order id: " + evt.orderId);
			double totalprice = 0;
			foreach (BasketItem item in evt.basket)
            {
				totalprice += item.product.price * item.amount;
            }
			Payment newPayment = new Payment()
			{
				PaymentId = Guid.NewGuid(),
				OrderId = evt.orderId,
				PaymentType = evt.paymentType,
				Amount = totalprice,
				PaymentState = "Registered",
				PaymentRecievedDate = new DateTime()
			};

            _paymentRepository.Save(newPayment);
			EventLogContext _eventLogDb = _scope.ServiceProvider.GetRequiredService<EventLogContext>();

			// publish PaymentRegistered Event
			PaymentRegistered paymentRegisteredEvent = new PaymentRegistered(newPayment);
			await _messagePublisher.PublishMessageAsync(paymentRegisteredEvent);
			await _eventLogDb.LogEvent(paymentRegisteredEvent);
		}
	}
}
