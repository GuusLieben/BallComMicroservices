using BallOrder.Models;
using System;
using BallOrderDomain.Events.Incoming;

namespace BallOrderDomain.Events.Outgoing
{
    public class OrderSetReadyForPicking : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid OrderId { get; set; }
        public OrderState OrderState { get; set; }
        public Guid ProductId { get; set; }
        public string EventName => "OrderSetReadyForPicking";
    }
}