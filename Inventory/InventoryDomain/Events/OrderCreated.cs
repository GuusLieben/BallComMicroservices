using System;

namespace InventoryDomain.Events
{
	public class OrderCreated : IEvent
	{
		public string EventName => "OrderCreated";
		public Guid EventId { get; set; }
	}
}
