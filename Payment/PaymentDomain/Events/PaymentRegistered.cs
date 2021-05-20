using System;
using System.Collections.Generic;
using PaymentDomain.Models;

namespace PaymentDomain.Events
{
    public class PaymentRegistered : IEvent
    {
        public Guid PaymentId;
        public Guid OrderId;
        public string PaymentType;
        public double Amount;
        public string PaymentState;
        public DateTime CreationDate;

        public string EventName => "PaymentCreated";

        public Guid EventId { get; set; } = Guid.NewGuid();
    }
}
