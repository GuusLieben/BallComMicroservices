using System;
using System.Collections.Generic;
using PaymentDomain.Models;
namespace PaymentDomain.Services
{
    public interface IPaymentRepository
    {
        public void Save(Payment payment);
        public void Update(Payment payment);
        public void Delete(Payment payment);
        public IEnumerable<Payment> Get();
        public Payment Get(Guid paymentId);
    }
}
