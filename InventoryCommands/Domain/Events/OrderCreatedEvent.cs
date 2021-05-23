using Domain.Models;
using System;
using System.Collections.Generic;

namespace Domain.Events
{
	public class OrderCreatedEvent : IEvent
	{
		public string EventName => "OrderCreated";

		public Guid EventId { get; set; }
		public Guid ProductId { get; set; }

		public Guid OrderId { get; set; }
		public IEnumerable<BasketItem> Basket { get; set; }
	}
}
