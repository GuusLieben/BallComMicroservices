using System;
using System.Collections.Generic;
using System.Text;

namespace Domain.Events
{
	public class OutOfStockEvent : IEvent
	{
		public string EventName => "OutOfStock";

		public Guid EventId { get; set; } = Guid.NewGuid();
		public Guid ProductId { get; set; }
		public Guid OrderId { get; set; }

		/// <summary>
		/// Amount of product missing from stock
		/// </summary>
		public int Amount { get; set; }
	}
}
