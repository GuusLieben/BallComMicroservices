using System;

namespace Domain.Events
{
	public class StockRemovedEvent
	{
		public Guid ProductId { get; set; }
		public int Amount { get; set; }
	}
}
