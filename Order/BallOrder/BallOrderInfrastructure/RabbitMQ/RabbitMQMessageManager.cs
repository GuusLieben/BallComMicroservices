using System;
using BallOrderInfrastructure.RabbitMQ.Interfaces;
using System.Threading.Tasks;
using Microsoft.Extensions.Hosting;
using System.Threading;
using BallOrderDomain.Events.Incoming;
using BallOrderDomain.Services;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Linq;

namespace BallOrderInfrastructure.RabbitMQ
{
    public class RabbitMQMessageManager : IMessageManager, IHostedService
    {
		private readonly IMessageListener _messageListener;
        private readonly IOrderRepository _orderRepository;
        private readonly IOrderProductRepository _orderProductRepository;
        private readonly IServiceScope _scope;

        public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
        {
            _scope = scopeFactory.CreateScope();
            _messageListener = _scope.ServiceProvider.GetRequiredService<IMessageListener>();
            _orderRepository = _scope.ServiceProvider.GetRequiredService<IOrderRepository>();
            _orderProductRepository = _scope.ServiceProvider.GetRequiredService<IOrderProductRepository>();
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
                    case "ProductCreatedEvent":
                        await HandleAsync(messageObject.ToObject<ProductCreatedEvent>());
                        break;
                    case "ProductUpdatedEvent":
                        await HandleAsync(messageObject.ToObject<ProductUpdatedEvent>());
                        break;
                    case "ProductDeletedEvent":
                        await HandleAsync(messageObject.ToObject<ProductDeletedEvent>());
                        break;
                    case "StockAddedEvent":
                        await HandleAsync(messageObject.ToObject<StockAddedEvent>());
                        break;
                    case "StockRemovedEvent":
                        await HandleAsync(messageObject.ToObject<StockRemovedEvent>());
                        break;
                    case "OutOfStockEvent":
                        await HandleAsync(messageObject.ToObject<OutOfStockEvent>());
                        break;
                    case "OrderCreatedEvent":
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

        private Task HandleAsync(ProductCreatedEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(ProductUpdatedEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(ProductDeletedEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(StockAddedEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(StockRemovedEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(OutOfStockEvent evt)
        {
            return Task.CompletedTask;
        }

        private Task HandleAsync(OrderCreatedEvent evt)
        {
            return Task.CompletedTask;
        }



    }
}