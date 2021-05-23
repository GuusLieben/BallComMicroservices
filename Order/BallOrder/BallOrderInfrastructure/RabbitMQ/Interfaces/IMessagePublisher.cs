using BallOrderDomain.Events;
using System.Threading.Tasks;

namespace BallOrderInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessagePublisher
    {
        /// <summary>
        /// Publish a message.
        /// </summary>
        /// <param name="messageType">Type of the message.</param>
        /// <param name="message">The message to publish.</param>
        /// <param name="routingKey">The routingkey to use (RabbitMQ specific).</param>
        Task PublishMessageAsync(IEvent e);
    }
}