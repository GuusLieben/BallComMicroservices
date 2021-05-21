using PaymentDomain.Models;
using System;
using System.Collections.Generic;

namespace PaymentDomain.Events
{
    public class PaymentApproved : IEvent
    {
        public Guid PaymentId;
        public Guid OrderId;
        public PaymentType PaymentType;
        public double Amount;
        public string PaymentState;
        public DateTime CreationDate;

        public string EventName => "PaymentApproved";

        public Guid EventId { get; set; } = Guid.NewGuid();

        public PaymentApproved(Payment payment)
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
