using PaymentDomain.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace PaymentDomain.Events
{
    public class OrderCreatedEvent : IEvent
    {
        public Guid orderId;
        public PaymentType paymentType;
        public double totalPrice;
        public List<BasketItem> basket;

        public string EventName => "OrderCreated";

        public Guid EventId { get; set; }
    }
}
