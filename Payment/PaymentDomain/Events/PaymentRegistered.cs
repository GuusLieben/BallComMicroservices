using System;
using System.Collections.Generic;
using PaymentDomain.Models;

namespace PaymentDomain.Events
{
    public class PaymentRegistered
    {
        public Guid PaymentId;
        public Guid OrderId;
        public PaymentType PaymentType;
        public double Amount;
        public PaymentState PaymentState;
        public DateTime CreationDate;
        public DateTime PaymentRecievedDate;
        public Dictionary<string, object> Meta { get; set; }

        public PaymentRegistered()
        {
        }
    }
}
