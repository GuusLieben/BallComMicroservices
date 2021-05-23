using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using BallOrder.Models;
using BallOrderDomain.Events;
using BallOrderDomain.Events.Incoming;
using BallOrderDomain.Events.Outgoing;

namespace BallOrderInfrastructure.DataAccess
{
    public class OrderEventReplayer
    {
        private readonly WriteOrderDbContext _db;

        public OrderEventReplayer(WriteOrderDbContext dbContext)
        {
            _db = dbContext;
        }

        /// <summary>
        /// Replays all events associated with the product up until the specified date
        /// </summary>
        public OrderState GetOrderStatus(DateTime utcTime, Guid orderId)
        {
            IEnumerable<IEvent> events = _db.EventLog
                .Where(evt => evt.OnDateCreated <= utcTime && evt.OrderId.Equals(orderId))
                .OrderBy(evt => evt.OnDateCreated)
                .Select(evt => DeserializeToEvent(evt));

            OrderState orderState = 0;
            foreach (IEvent evt in events)
            {
                switch (evt.EventName)
                {
                    case "OrderSetReadyForPicking":
                        OrderSetReadyForPicking orderSetReadyForPicking = evt as OrderSetReadyForPicking;

                        orderState = orderSetReadyForPicking.OrderState;
                        break;
                }
            }

            return orderState;
        }

        public Dictionary<Guid, int> GetOrdersWithStatusWaiting(Guid productId)
        {
            IEnumerable<Guid> orderIds = _db.EventLog
                .Where(evt => evt.EventName.Equals("OutOfStock") && evt.ProductId.Equals(productId))
                .Select(evt => evt.OrderId);

            IEnumerable<IEvent> events = _db.EventLog
                .Where(evt => orderIds.Contains(evt.OrderId) && (evt.EventName.Equals("OutOfStock") || evt.EventName.Equals("StockClaimed"))
                                                             && evt.ProductId.Equals(productId))
                .OrderBy(evt => evt.OnDateCreated)
                .Select(evt => DeserializeToEvent(evt));

            Dictionary<Guid, int> orders = new Dictionary<Guid, int>();

            foreach (IEvent evt in events)
            {
                if (!orders.ContainsKey(evt.OrderId))
                {
                    orders.Add(evt.OrderId, 0);
                }
                switch (evt.EventName)
                {
                    case "StockClaimed":
                        StockClaimed stockClaimed = evt as StockClaimed;
                        orders[evt.OrderId] -= stockClaimed.Amount;
                        break;
                    case "OutOfStock":
                        OutOfStock outOfStock = evt as OutOfStock;
                        orders[evt.OrderId] += outOfStock.Amount;
                        break;
                }
            }

            return orders;
        }

        public bool OrderIsWaitingForStock(Guid orderId)
        {
            IEnumerable<IEvent> events = _db.EventLog
                .Where(evt => evt.OrderId.Equals(orderId) && (evt.EventName.Equals("OutOfStock") || evt.EventName.Equals("StockClaimed")))
                .OrderBy(evt => evt.OnDateCreated)
                .Select(evt => DeserializeToEvent(evt));

            Dictionary<Guid, int> products = new Dictionary<Guid, int>();
            
            foreach (IEvent evt in events)
            {
                if (!products.ContainsKey(evt.ProductId))
                {
                    products.Add(evt.ProductId, 0);
                }
                switch (evt.EventName)
                {
                    case "StockClaimed":
                        StockClaimed stockClaimed = evt as StockClaimed;
                        products[evt.ProductId] -= stockClaimed.Amount;
                        break;
                    case "OutOfStock":
                        OutOfStock outOfStock = evt as OutOfStock;
                        products[evt.ProductId] += outOfStock.Amount;
                        break;
                }
            }

            bool waitingForStock = false;
            foreach (int amount in products.Values)
            {
                if (amount > 0)
                {
                    waitingForStock = true;
                }
            }

            return waitingForStock;
        }

        private static IEvent DeserializeToEvent(EventLog log)
        {
            return log.EventName switch
            {
                "OrderPicked" => JsonConvert.DeserializeObject<OrderPicked>(log.EventJson),
                "StockClaimed" => JsonConvert.DeserializeObject<StockClaimed>(log.EventJson),
                "OutOfStock" => JsonConvert.DeserializeObject<OutOfStock>(log.EventJson),
                _ => null,
            };
        }
    }
}