using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryDomain.Models
{
	public class Supplier
	{
		public Guid SupplierId { get; set; }
		public string Email { get; set; }
		public string Name { get; set; }
	}
}
