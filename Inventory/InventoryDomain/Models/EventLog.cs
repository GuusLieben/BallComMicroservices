using System;

namespace InventoryDomain.Models
{
	public class EventLog
	{
		public DateTime CreatedOnDate { get; set; } = DateTime.UtcNow;
		public string EventName { get; set; }
		public string EventJson { get; set; }
	}
}
