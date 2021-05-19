using System;
using System.ComponentModel.DataAnnotations;

namespace InventoryDomain.Models
{
	public class ProcessedEvent
	{
		[Key]
		public Guid EventId { get; set; }
	}
}
