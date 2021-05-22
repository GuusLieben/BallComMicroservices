using Domain;
using Domain.Events;
using Infrastructure.DataStorage;
using Infrastructure.RabbitMQ.Interfaces;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Infrastructure.RabbitMQ
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
						await HandleAsync(messageObject.ToObject<ProductCreatedEvent>());
						break;

					case "StockAdded":
						await HandleAsync(messageObject.ToObject<StockAddedEvent>());
						break;

					case "StockRemoved":
						await HandleAsync(messageObject.ToObject<StockRemovedEvent>());
						break;

					default:
						break;
				}
			}
			catch (Exception)
			{
				return false;
			}

			return await Task.FromResult(true);
		}

		private async Task HandleAsync(ProductCreatedEvent productCreated)
		{
			ProductDbContext db = _scope.ServiceProvider.GetRequiredService<ProductDbContext>();
			await db.Product.AddAsync(new Product(productCreated));
			await db.SaveChangesAsync();
		}

		private async Task HandleAsync(StockAddedEvent stockAdded)
		{
			ProductDbContext db = _scope.ServiceProvider.GetRequiredService<ProductDbContext>();
			Product p = await db.Product.FindAsync(stockAdded.ProductId);

			if(p != null)
			{
				p.Amount += stockAdded.Amount;
				await db.SaveChangesAsync();
			}
		}

		private async Task HandleAsync(StockRemovedEvent stockRemoved)
		{
			ProductDbContext db = _scope.ServiceProvider.GetRequiredService<ProductDbContext>();
			Product p = await db.Product.FindAsync(stockRemoved.ProductId);

			if(p != null)
			{
				p.Amount -= stockRemoved.Amount;
				await db.SaveChangesAsync();
			}
		}
	}
}
