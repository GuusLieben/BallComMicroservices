using System;

namespace Domain.Events
{
	public class StockClaimedEvent : IEvent
	{
		public string EventName => "StockClaimed";

		public Guid EventId { get; set; }
		public Guid ProductId { get; set; }
		public Guid OrderId { get; set; }

		public int Amount { get; set; }
	}
}
