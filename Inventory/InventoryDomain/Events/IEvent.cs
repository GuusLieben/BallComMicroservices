using System;

namespace InventoryDomain.Events
{
	public interface IEvent
	{
		string EventName { get; }
		Guid EventId { get; set; }
	}
}
