using System;

namespace Domain.Events
{
	public class StockAddedEvent
	{
		public Guid ProductId { get; set; }
		public int Amount { get; set; }
	}
}
