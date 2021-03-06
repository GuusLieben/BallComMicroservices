using System;
namespace PaymentDomain.Models
{
    public class Payment
    {
        public Guid PaymentId { get; set; }
        public Guid OrderId { get; set; }
        public PaymentType PaymentType { get; set; }
        public double Amount { get; set; }
        public string PaymentState { get; set; }
        public DateTime CreationDate { get; set; } = DateTime.UtcNow;
        public DateTime PaymentRecievedDate { get; set; }
    }
}
