using PaymentDomain.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace PaymentDomain.Events
{
    public class OrderCreated : IEvent
    {
        public Guid OrderId;
        public double TotalAmount;
        public string PaymentType;

        public string EventName => "OrderCreated";

        public Guid EventId { get; set; }
    }
}
