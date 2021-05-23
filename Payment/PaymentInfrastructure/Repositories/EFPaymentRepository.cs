using PaymentDomain.Models;
using PaymentDomain.Services;
using PaymentInfrastructure.Context;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PaymentInfrastructure.Repositories
{
    public class EFPaymentRepository : IPaymentRepository
    {
        private readonly PaymentDbContext _dbContext;

        public EFPaymentRepository(PaymentDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public void Delete(Payment payment)
        {
            _dbContext.Payments.Remove(payment);
            _dbContext.SaveChanges();
        }

        public IEnumerable<Payment> Get()
        {
            return _dbContext.Payments.ToList();
        }

        public Payment Get(Guid paymentId)
        {
            IEnumerable<Payment> matches = _dbContext.Payments.Where(payment => payment.PaymentId.Equals(paymentId)).ToList();
            return matches.Any() ? matches.ElementAt(0) : null;
        }

        public void Save(Payment payment)
        {
            Payment existingPayment = _dbContext.Payments.FirstOrDefault(p => p.PaymentId == payment.PaymentId);
            if (existingPayment == null)
            {
                _dbContext.Payments.Add(payment);
            }
            _dbContext.SaveChanges();
        }
        public void Update(Payment payment)
        {
            _dbContext.Entry(payment).CurrentValues.SetValues(payment);
            _dbContext.SaveChanges();
        }
    }
}
