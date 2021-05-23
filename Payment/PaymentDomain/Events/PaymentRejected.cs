using PaymentDomain.Models;
using System;
using System.Collections.Generic;

namespace PaymentDomain.Events
{
    public class PaymentRejected : IEvent
    {
        public Guid PaymentId;
        public Guid OrderId;
        public PaymentType PaymentType;
        public double Amount;
        public string PaymentState;
        public DateTime CreationDate;

        public string EventName => "PaymentRejected";

        public Guid EventId { get; set; } = Guid.NewGuid();

        public PaymentRejected(Payment payment)
        {
            PaymentId = payment.PaymentId;
            OrderId = payment.OrderId;
            PaymentType = payment.PaymentType;
            Amount = payment.Amount;
            PaymentState = payment.PaymentState;
            CreationDate = payment.CreationDate;
        }
    }
}
