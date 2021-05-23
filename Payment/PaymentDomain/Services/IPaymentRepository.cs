using System;
using System.Collections.Generic;
using PaymentDomain.Models;
namespace PaymentDomain.Services
{
    public interface IPaymentRepository
    {
        void Save(Payment payment);
        void Update(Payment payment);
        void Delete(Payment payment);
        IEnumerable<Payment> Get();
        Payment Get(Guid paymentId);
    }
}
