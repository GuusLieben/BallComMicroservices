using InventoryDomain.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryDomain.Services
{
	public interface ISupplierRepository
	{
		public void Save(Supplier supplier);
		public void Update(Supplier supplier);
		public void Delete(Supplier supplier);
		public IEnumerable<Supplier> Get();
		public Supplier Get(Guid supplierId);
	}
}
