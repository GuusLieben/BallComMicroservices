using Domain.Events;
using System.Threading.Tasks;

namespace Infrastructure.RabbitMQ.Interfaces
{
    public interface IMessagePublisher
    {
        Task PublishMessageAsync(IEvent e);
    }
}
