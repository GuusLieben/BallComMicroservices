using System;

namespace Domain.Events
{
	public interface IEvent
	{
		string EventName { get; }
		Guid EventId { get; set; }

		/// <summary>
		/// ID of the affected product (if a product is affected)
		/// </summary>
		Guid ProductId { get; set; }

		/// <summary>
		/// ID of the affected product (if a product is affected)
		/// </summary>
		Guid OrderId { get; set; }
	}
}
