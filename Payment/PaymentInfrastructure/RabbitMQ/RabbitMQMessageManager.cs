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
					case "PaymentRegistered":
						await HandleAsync(messageObject.ToObject<PaymentRegistered>());
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

		private Task HandleAsync(PaymentRegistered evt)
		{
			Debug.WriteLine("-------------------------------------------");
			//Debug.WriteLine("SupplierId: " + evt.Guid);
			//Debug.WriteLine("Email: " + evt.Email);
			//Debug.WriteLine("Name: " + evt.Meta["company"]);
			Debug.WriteLine("-------------------------------------------");

			Payment newPayment = new Payment()
			{
				//PaymentId = evt.Guid,
				//Email = evt.Email,
				//Name = evt.Meta.ContainsKey("company") ? evt.Meta["company"].ToString() : "UNKNOWN"
			};

			_paymentRepository.Save(newPayment);

			return Task.CompletedTask;
		}
	}
}
