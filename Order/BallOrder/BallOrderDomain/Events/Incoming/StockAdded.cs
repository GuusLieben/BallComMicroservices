using System;

namespace BallOrderDomain.Events.Incoming
{
    public class StockAdded : IEvent
    {
        public Guid EventId { get; set; }
        public Guid ProductId { get; set; }
        public Guid OrderId { get; set; }
        public int Amount { get; set; }
        public string EventName => "StockAdded";
    }
}