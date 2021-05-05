using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryDomain.Events
{
	public class SupplierAdded
	{
		public Guid Guid { get; set; }
		public string Email { get; set; }
		public Dictionary<string, object> Meta { get; set; }
	}
}
