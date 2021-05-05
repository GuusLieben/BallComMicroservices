﻿using InventoryDomain.Events;
using InventoryDomain.Models;
using InventoryDomain.Services;
using InventoryInfrastructure.RabbitMQ.Interfaces;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ
{
	public class RabbitMQMessageManager : IMessageHandlerCallback, IHostedService
	{
		private readonly IMessageHandler _messageHandler;
		private readonly ISupplierRepository _supplierRepository;
		private readonly IServiceScope _scope;

		public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
		{
			_scope = scopeFactory.CreateScope();
			_messageHandler = _scope.ServiceProvider.GetRequiredService<IMessageHandler>();
			_supplierRepository = _scope.ServiceProvider.GetRequiredService<ISupplierRepository>();
		}


		public Task StartAsync(CancellationToken cancellationToken)
		{
			_messageHandler.Start(this);
			return Task.CompletedTask;
		}

		public Task StopAsync(CancellationToken cancellationToken)
		{
			_messageHandler.Stop();
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
					case "SupplierAdded":
						await HandleAsync(messageObject.ToObject<SupplierAdded>());
						break;
					default:
						break;
				}
			}
			catch(Exception)
			{
				return false;
			}

            return true;
        }

		private Task HandleAsync(SupplierAdded evt)
		{
			Debug.WriteLine("-------------------------------------------");
			Debug.WriteLine("SupplierId: " + evt.Guid);
			Debug.WriteLine("Email: " + evt.Email);
			Debug.WriteLine("Name: " +  evt.Meta["company"]);
			Debug.WriteLine("-------------------------------------------");

			Supplier newSupplier = new Supplier()
			{
				SupplierId = evt.Guid,
				Email = evt.Email,
				Name = evt.Meta.ContainsKey("company") ? evt.Meta["company"].ToString() : "UNKNOWN"
			};

			_supplierRepository.Save(newSupplier);

			return Task.CompletedTask;
		}
	}
}
