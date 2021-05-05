using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
	public interface IMessageHandlerCallback
	{
		Task<bool> HandleMessageAsync(string messageType, string message);
	}
}
