using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PaymentInfrastructure.Interfaces
{
    public interface IMessageHandler
    {
        Task<bool> HandleMessageAsync(string messageType, string message);
    }
}
