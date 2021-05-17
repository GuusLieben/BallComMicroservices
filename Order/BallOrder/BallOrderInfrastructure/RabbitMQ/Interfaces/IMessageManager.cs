using System.Threading.Tasks;

namespace BallOrderInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessageManager
    {
        Task<bool> HandleMessageAsync(string messageType, string message);
    }
}