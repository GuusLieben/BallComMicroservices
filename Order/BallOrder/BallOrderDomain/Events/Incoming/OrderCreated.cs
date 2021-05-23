using BallOrder.Models;
using System;
using System.Collections.Generic;

namespace BallOrderDomain.Events.Incoming
{
    public class OrderCreated : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid CustomerId { get; set; }
        public Guid OrderId { get; set; }
        public IEnumerable<Basket> Basket { get; set; }
        public Guid ProductId { get; set; }
        public string EventName => "OrderCreated";

    }
}