using System;

namespace BallOrderDomain.Events.Incoming
{
    public class PaymentApproved : IEvent
    {
        public Guid EventId { get; set; }
        public Guid OrderId { get; set; }
        public Guid ProductId { get; set; }
        public string EventName => "PaymentApproved";
    }
}