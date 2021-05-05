using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessagePublisher
    {
        /// <summary>
        /// Publish a message.
        /// </summary>
        /// <param name="messageType">Type of the message.</param>
        /// <param name="message">The message to publish.</param>
        /// <param name="routingKey">The routingkey to use (RabbitMQ specific).</param>
        Task PublishMessageAsync(string messageType, object message, string routingKey);
    }
}
