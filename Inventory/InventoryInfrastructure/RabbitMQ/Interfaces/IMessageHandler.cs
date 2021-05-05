using System.Threading.Tasks;

namespace InventoryInfrastructure.RabbitMQ.Interfaces
{
	public interface IMessageHandler
	{
		Task<bool> HandleMessageAsync(string messageType, string message);
	}
}
