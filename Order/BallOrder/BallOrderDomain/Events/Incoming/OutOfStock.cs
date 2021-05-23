using System;

namespace BallOrderDomain.Events.Incoming
{
    public class OutOfStock : IEvent
    {
        public Guid EventId { get; set; }
        public Guid OrderId { get; set; }
        public Guid ProductId { get; set; }
        public int Amount { get; set; }
        public string EventName => "OutOfStock";
    }
}