using System;
using System.ComponentModel.DataAnnotations;

namespace InventoryDomain.Models
{
	public class EventLog
	{
		[Key]
		public Guid EventId { get; set; } = Guid.NewGuid();
		public DateTime CreatedOnDate { get; set; } = DateTime.UtcNow;

		public string EventName { get; set; }
		public string EventJson { get; set; }
	}
}
