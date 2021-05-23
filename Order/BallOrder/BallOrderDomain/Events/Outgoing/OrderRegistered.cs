using BallOrder.Models;
using System;
using System.Collections.Generic;

namespace BallOrderDomain.Events.Outgoing
{
    public class OrderRegistered : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public Guid ProductId { get; set; }
        public OrderState OrderState { get; set; }
        public IEnumerable<Basket> Basket { get; set; }
        public string EventName => "OrderRegistered";
        
    }
}