using System;
using BallOrderDomain.Events.Incoming;

namespace BallOrderDomain.Events.Outgoing
{
    public class StockClaimed : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid OrderId { get; set; }
        public Guid ProductId { get; set; }
        public int Amount { get; set; }
        public string EventName => "StockClaimed";
    }
}