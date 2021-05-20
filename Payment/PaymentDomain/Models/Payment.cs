using System;
namespace PaymentDomain.Models
{
    public class Payment
    {
        public Guid PaymentId { get; set; }
        public Guid OrderId { get; set; }
        public string PaymentType { get; set; }
        public double Amount { get; set; }
        public string PaymentState { get; set; }
        public DateTime CreationDate { get; set; }
        public DateTime PaymentRecievedDate { get; set; }
    }
}
