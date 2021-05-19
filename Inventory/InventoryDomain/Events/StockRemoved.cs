using System;

namespace InventoryDomain.Events
{
	public class StockRemoved : IEvent
	{
		public Guid EventId { get; set; } = Guid.NewGuid();

		public Guid ProductId { get; set; }
		public int Amount { get; set; }

		public string EventName => "StockAdded";
	}
}
