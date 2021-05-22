using InventoryDomain.Events;
using InventoryDomain.Models;
using InventoryInfrastructure.RabbitMQ.Interfaces;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ
{
	public class RabbitMQMessageManager : IMessageHandler, IHostedService
	{
		private readonly IMessageListener _messageListener;
		private readonly IServiceScope _scope;

		public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
		{
			_scope = scopeFactory.CreateScope();
			_messageListener = _scope.ServiceProvider.GetRequiredService<IMessageListener>();
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
					case "ProductCreated":
						await HandleAsync(messageObject.ToObject<ProductCreated>());
						break;

					case "StockAdded":
						await HandleAsync(messageObject.ToObject<StockAdded>());
						break;

					case "StockRemoved":
						await HandleAsync(messageObject.ToObject<StockRemoved>());
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

		private async Task HandleAsync(ProductCreated createdEvent)
		{
			ReadDbContext _readDb = _scope.ServiceProvider.GetRequiredService<ReadDbContext>();

			if (await _readDb.ProcessedEvent.FindAsync(createdEvent.EventId) != null)
				return;

			Product createdProduct = new Product(createdEvent);
			_readDb.Product.Add(createdProduct);
			_readDb.ProcessedEvent.Add(new ProcessedEvent() { EventId = createdEvent.EventId });

			await _readDb.SaveChangesAsync();
		}

		private async Task HandleAsync(StockAdded saEvent)
		{
			ReadDbContext _readDb = _scope.ServiceProvider.GetRequiredService<ReadDbContext>();

			if (await _readDb.ProcessedEvent.FindAsync(saEvent.EventId) != null)
				return;

			Product updatedProduct = await _readDb.Product.FindAsync(saEvent.ProductId);
			updatedProduct.Amount += saEvent.Amount;

			_readDb.ProcessedEvent.Add(new ProcessedEvent() { EventId = saEvent.EventId });

			await _readDb.SaveChangesAsync();
		}

		private async Task HandleAsync(StockRemoved srEvent)
		{
			ReadDbContext _readDb = _scope.ServiceProvider.GetRequiredService<ReadDbContext>();

			if (await _readDb.ProcessedEvent.FindAsync(srEvent.EventId) != null)
				return;

			Product updatedProduct = await _readDb.Product.FindAsync(srEvent.ProductId);
			updatedProduct.Amount -= srEvent.Amount;

			_readDb.ProcessedEvent.Add(new ProcessedEvent() { EventId = srEvent.EventId });

			await _readDb.SaveChangesAsync();
		}
	}
}
