using BallOrderDomain.Events.Incoming;
using BallOrderDomain.Events.Outgoing;
using BallOrderDomain.Services;
using BallOrderInfrastructure.RabbitMQ.Interfaces;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Reflection.Metadata;
using System.Threading;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderInfrastructure.DataAccess;

namespace BallOrderInfrastructure.RabbitMQ
{
    public class RabbitMQMessageManager : IMessageManager, IHostedService
    {
        private readonly IMessageListener _messageListener;
        private readonly IMessagePublisher _messagePublisher;
        private readonly IOrderWriteRepository _orderWriteRepository;
        private readonly IOrderReadProductRepository _orderReadProductRepository;
        private readonly IOrderReadOrderProductRepository _orderReadOrderProductRepository;
        private readonly IOrderReadOrderRepository _orderReadOrderRepository;
        private readonly IServiceScope _scope;
        private readonly OrderEventReplayer _orderEventReplayer;

        public RabbitMQMessageManager(IServiceScopeFactory scopeFactory)
        {
            _scope = scopeFactory.CreateScope();
            _messageListener = _scope.ServiceProvider.GetRequiredService<IMessageListener>();
            _messagePublisher = _scope.ServiceProvider.GetRequiredService<IMessagePublisher>();
            _orderWriteRepository = _scope.ServiceProvider.GetRequiredService<IOrderWriteRepository>();
            _orderReadProductRepository = _scope.ServiceProvider.GetRequiredService<IOrderReadProductRepository>();
            _orderReadOrderProductRepository = _scope.ServiceProvider.GetRequiredService<IOrderReadOrderProductRepository>();
            _orderReadOrderRepository = _scope.ServiceProvider.GetRequiredService<IOrderReadOrderRepository>();
            _orderEventReplayer = _scope.ServiceProvider.GetRequiredService<OrderEventReplayer>();
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
                    case "OutOfStock":
                        await HandleAsync(messageObject.ToObject<OutOfStock>());
                        break;
                    case "OrderCreated":
                        await HandleAsync(messageObject.ToObject<OrderCreated>());
                        break;
                    case "PaymentApproved":
                        await HandleAsync(messageObject.ToObject<PaymentApproved>());
                        break;
                    case "StockClaimed":
                        await HandleAsync(messageObject.ToObject<StockClaimed>());
                        break;
                    default:
                        break;
                }
            }
            catch (Exception e)
            {
                return false;
            }

            return true;
        }

        private async Task HandleAsync(ProductCreated evt)
        {
            await _orderReadProductRepository.AddProduct(new Product(evt));

            await _orderWriteRepository.AddEventLog(evt);
        }

        private async Task HandleAsync(StockAdded evt)
        {
            await _orderReadProductRepository.AddAmount(evt.ProductId, evt.Amount);

            await _orderWriteRepository.AddEventLog(evt);

            Dictionary<Guid, int> orders = _orderEventReplayer.GetOrdersWithStatusWaiting(evt.ProductId);

            int availableStock = evt.Amount;

            foreach (KeyValuePair<Guid, int> order in orders)
            {
                
                if (order.Value == 0)
                {
                    continue;
                }

                StockClaimed stockClaimed = null;

                if (availableStock == order.Value)
                {
                    stockClaimed = new StockClaimed()
                    {
                        OrderId = evt.OrderId,
                        ProductId = evt.ProductId,
                        Amount = availableStock
                    };
                    availableStock = 0;

                }
                else if (availableStock < order.Value)
                {
                    stockClaimed = new StockClaimed()
                    {
                        OrderId = evt.OrderId,
                        ProductId = evt.ProductId,
                        Amount = availableStock
                    };
                    availableStock = 0;
                }
                else 
                {
                    stockClaimed = new StockClaimed()
                    {
                        OrderId = evt.OrderId,
                        ProductId = evt.ProductId,
                        Amount = order.Value
                    };
                    availableStock -= order.Value;
                }

                await _orderWriteRepository.AddEventLog(stockClaimed);
                await _messagePublisher.PublishMessageAsync(stockClaimed);

                if (availableStock == 0)
                {
                    break;
                }
            }
        }

        private async Task HandleAsync(StockRemoved evt)
        {
            await _orderReadProductRepository.RemoveAmount(evt.ProductId, evt.Amount);

            await _orderWriteRepository.AddEventLog(evt);
        }

        private async Task HandleAsync(OutOfStock evt)
        {
            await _orderWriteRepository.AddEventLog(evt);

            OrderPutOnHold orderPutOnHold = new OrderPutOnHold(evt);

            await _orderReadOrderRepository.UpdateOrderStatus(orderPutOnHold.OrderId, orderPutOnHold.OrderState);

            await _orderWriteRepository.AddEventLog(orderPutOnHold);

            await _messagePublisher.PublishMessageAsync(orderPutOnHold);
        }

        private async Task HandleAsync(OrderCreated evt)
        {
            Order order = new Order(evt);
            await _orderReadOrderRepository.AddOrder(order);

            foreach (Basket basket in evt.Basket)
            {
                await _orderReadOrderProductRepository.AddOrderProduct(new OrderProduct(order, basket));
            }

            await _orderWriteRepository.AddEventLog(evt);

            OrderRegistered orderRegistered = new OrderRegistered()
            {
                OrderId = evt.OrderId,
                CustomerId = evt.CustomerId,
                OrderState = 0,
                Basket = evt.Basket
            };

            await _orderWriteRepository.AddEventLog(orderRegistered);
            await _messagePublisher.PublishMessageAsync(orderRegistered);
        }

        private async Task HandleAsync(PaymentApproved evt)
        {
            await _orderWriteRepository.AddEventLog(evt);

            OrderSetReadyForPicking orderSetReadyForPicking = new OrderSetReadyForPicking()
            {
                OrderId = evt.OrderId,
                OrderState = OrderState.ReadyForPicking,
            };

            await _orderReadOrderRepository.UpdateOrderStatus(orderSetReadyForPicking.OrderId,
                orderSetReadyForPicking.OrderState);
            await _orderWriteRepository.AddEventLog(orderSetReadyForPicking);
            await _messagePublisher.PublishMessageAsync(orderSetReadyForPicking);
        }

        private async Task HandleAsync(StockClaimed evt)
        {
            if (!_orderEventReplayer.OrderIsWaitingForStock(evt.OrderId))
            {
                OrderSetReadyForPicking orderSetReadyForPicking = new OrderSetReadyForPicking
                {
                    OrderId = evt.OrderId,
                    OrderState = OrderState.ReadyForPicking,
                };

                await _orderReadOrderRepository.UpdateOrderStatus(orderSetReadyForPicking.OrderId,
                    orderSetReadyForPicking.OrderState);

                await _orderWriteRepository.AddEventLog(orderSetReadyForPicking);
                await _messagePublisher.PublishMessageAsync(orderSetReadyForPicking);
            }
        }
    }
}