using System;

namespace Domain.Events
{
	public class StockRemovedEvent : IEvent
	{
		public Guid EventId { get; set; } = Guid.NewGuid();
		public Guid OrderId { get; set; }

		public Guid ProductId { get; set; }
		public int Amount { get; set; }

		public string EventName => "StockRemoved";
	}
}
