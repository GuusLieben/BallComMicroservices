using Domain.Events;
using Domain.Models;
using Infrastructure.DataStorage;
using Infrastructure.RabbitMQ.Interfaces;
using Infrastructure.Services;
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
					case "OrderCreated":
						await HandleAsync(messageObject.ToObject<OrderCreatedEvent>());
						break;

					case "PaymentApproved":
						await HandleAsync(messageObject.ToObject<PaymentApprovedEvent>());
						break;

					case "StockClaimed":
						await HandleAsync(messageObject.ToObject<StockClaimedEvent>());
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

		private async Task HandleAsync(OrderCreatedEvent orderCreated)
		{
			EventStoreDbContext db = _scope.ServiceProvider.GetRequiredService<EventStoreDbContext>();
			await db.LogEvent(orderCreated);
		}

		private async Task HandleAsync(PaymentApprovedEvent paymentApproved)
		{
			EventStoreDbContext db = _scope.ServiceProvider.GetRequiredService<EventStoreDbContext>();
			ProductEventReplayer replayer = _scope.ServiceProvider.GetRequiredService<ProductEventReplayer>();
			IMessagePublisher _messagePublisher = _scope.ServiceProvider.GetRequiredService<IMessagePublisher>();

			await db.LogEvent(paymentApproved);

			EventLog loggedEvent = db.EventLog.FirstOrDefault(log => log.EventName.Equals("OrderCreated") && log.OrderId.Equals(paymentApproved.OrderId));

			if (loggedEvent == null)
				return;

			OrderCreatedEvent ocEvent = JsonConvert.DeserializeObject<OrderCreatedEvent>(loggedEvent.EventJson);
			foreach(BasketItem item in ocEvent.Basket)
			{
				int amount = item.Amount;
				Guid productId = item.Product.ProductId;

				(bool productFound, int currentAmount) = replayer.GetProductAmountOn(DateTime.UtcNow, productId);
				if (!productFound)
				{
					OutOfStockEvent oosEvent = new OutOfStockEvent()
					{
						ProductId = productId,
						Amount = amount,
						OrderId = ocEvent.OrderId
					};
					await db.LogEvent(oosEvent);
					await _messagePublisher.PublishMessageAsync(oosEvent);

					continue;
				}

				if(currentAmount < amount)
				{
					int removableAmount = currentAmount;
					int missingAmount = amount - currentAmount;

					OutOfStockEvent oosEvent = new OutOfStockEvent()
					{
						ProductId = productId,
						Amount = missingAmount,
						OrderId = ocEvent.OrderId
					};

					StockRemovedEvent srEvent = new StockRemovedEvent()
					{
						ProductId = productId,
						Amount = removableAmount
					};

					await db.LogEvent(oosEvent);
					await db.LogEvent(srEvent);
					await _messagePublisher.PublishMessageAsync(oosEvent);
					await _messagePublisher.PublishMessageAsync(srEvent);
					continue;
				}

				StockRemovedEvent evt = new StockRemovedEvent()
				{
					ProductId = productId,
					Amount = amount
				};

				await db.LogEvent(evt);
				await _messagePublisher.PublishMessageAsync(evt);
			}
		}

		private async Task HandleAsync(StockClaimedEvent stockClaimed)
		{
			EventStoreDbContext db = _scope.ServiceProvider.GetRequiredService<EventStoreDbContext>();
			ProductEventReplayer replayer = _scope.ServiceProvider.GetRequiredService<ProductEventReplayer>();
			IMessagePublisher _messagePublisher = _scope.ServiceProvider.GetRequiredService<IMessagePublisher>();

			await db.LogEvent(stockClaimed);

			int amount = stockClaimed.Amount;
			Guid productId = stockClaimed.ProductId;

			(bool productFound, int currentAmount) = replayer.GetProductAmountOn(DateTime.UtcNow, productId);
			if (!productFound)
			{
				OutOfStockEvent oosEvent = new OutOfStockEvent()
				{
					ProductId = productId,
					Amount = amount,
					OrderId = stockClaimed.OrderId
				};
				await db.LogEvent(oosEvent);
				await _messagePublisher.PublishMessageAsync(oosEvent);

				return;
			}

			if (currentAmount < amount)
			{
				int removableAmount = currentAmount;
				int missingAmount = amount - currentAmount;

				OutOfStockEvent oosEvent = new OutOfStockEvent()
				{
					ProductId = productId,
					Amount = missingAmount,
					OrderId = stockClaimed.OrderId
				};

				StockRemovedEvent srEvent = new StockRemovedEvent()
				{
					ProductId = productId,
					Amount = removableAmount
				};

				await db.LogEvent(oosEvent);
				await db.LogEvent(srEvent);
				await _messagePublisher.PublishMessageAsync(oosEvent);
				await _messagePublisher.PublishMessageAsync(srEvent);

				return;
			}

			StockRemovedEvent evt = new StockRemovedEvent()
			{
				ProductId = productId,
				Amount = amount
			};

			await db.LogEvent(evt);
			await _messagePublisher.PublishMessageAsync(evt);
		}
	}
}
