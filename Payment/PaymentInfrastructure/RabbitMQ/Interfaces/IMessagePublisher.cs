using PaymentDomain.Events;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PaymentInfrastructure.Interfaces
{
    public interface IMessagePublisher
    {
        Task PublishMessageAsync(IEvent evt);
    }
}
