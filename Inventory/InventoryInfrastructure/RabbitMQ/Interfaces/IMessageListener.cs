using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessageListener
    {
        void Start(IMessageHandler callback);
        void Stop();
    }
}
