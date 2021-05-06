using System;
using System.Collections.Generic;
using System.Text;

namespace PaymentInfrastructure.Interfaces
{
    public interface IMessageListener
    {
        void Start(IMessageHandler callback);
        void Stop();
    }
}
