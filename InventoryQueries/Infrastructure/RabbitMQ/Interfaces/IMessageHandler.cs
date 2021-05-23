using System.Threading.Tasks;

namespace Infrastructure.RabbitMQ.Interfaces
{
	public interface IMessageHandler
	{
		Task<bool> HandleMessageAsync(string messageType, string message);
	}
}
