using System;
using System.ComponentModel.DataAnnotations;

namespace Domain.Models
{
	public class EventLog
	{
		[Key]
		public Guid EventId { get; set; } = Guid.NewGuid();
		public DateTime CreatedOnDate { get; set; } = DateTime.UtcNow;

		/// <summary>
		/// The ID of the product which is affected by the event
		/// </summary>
		public Guid ProductId { get; set; }

		/// <summary>
		/// ID of the order which is affected by the event
		/// </summary>
		public Guid OrderId { get; set; }

		public string EventName { get; set; }
		public string EventJson { get; set; }
	}
}
