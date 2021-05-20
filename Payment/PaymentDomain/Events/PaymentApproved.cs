using PaymentDomain.Models;
using System;
using System.Collections.Generic;

namespace PaymentDomain.Events
{
    public class PaymentApproved : IEvent
    {
        public Guid PaymentId;
        public Guid OrderId;
        public string PaymentType;
        public double Amount;
        public string PaymentState;
        public DateTime CreationDate;

        public string EventName => "PaymentApproved";

        public Guid EventId { get; set; } = Guid.NewGuid();

    }
}
