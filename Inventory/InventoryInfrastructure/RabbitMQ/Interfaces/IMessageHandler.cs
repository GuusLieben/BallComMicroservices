using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessageHandler
    {
        void Start(IMessageHandlerCallback callback);
        void Stop();
    }
}
