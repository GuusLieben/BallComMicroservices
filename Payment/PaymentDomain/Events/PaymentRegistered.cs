using System;
using System.Collections.Generic;
using PaymentDomain.Models;

namespace PaymentDomain.Events
{
    public class PaymentRegistered : IEvent
    {
        public Guid PaymentId;
        public Guid OrderId;
        public PaymentType PaymentType;
        public double Amount;
        public string PaymentState;
        public DateTime CreationDate;

        public string EventName => "PaymentCreated";

        public Guid EventId { get; set; } = Guid.NewGuid();
        
       public PaymentRegistered(Payment payment)
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
