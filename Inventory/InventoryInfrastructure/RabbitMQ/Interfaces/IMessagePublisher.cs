using InventoryDomain.Events;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessagePublisher
    {
        Task PublishMessageAsync(IEvent e);
    }
}
