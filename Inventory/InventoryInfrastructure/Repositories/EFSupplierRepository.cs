using InventoryDomain.Models;
using InventoryDomain.Services;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace InventoryInfrastructure.Repositories
{
	public class EFSupplierRepository : ISupplierRepository
	{
		private readonly InventoryDbContext _dbContext;

		public EFSupplierRepository(InventoryDbContext dbContext)
		{
			_dbContext = dbContext;
		}

		public void Delete(Supplier supplier)
		{
			_dbContext.Supplier.Remove(supplier);
			_dbContext.SaveChanges();
		}

		public IEnumerable<Supplier> Get()
		{
			return _dbContext.Supplier.ToList();
		}

		public Supplier Get(Guid supplierId)
		{
			IEnumerable<Supplier> matches = _dbContext.Supplier.Where(supplier => supplier.SupplierId.Equals(supplierId)).ToList();

			return matches.Any() ? matches.ElementAt(0) : null;
		}

		public void Save(Supplier supplier)
		{
			Supplier trackedSupplier = _dbContext.Supplier.FirstOrDefault(sup => sup.SupplierId == supplier.SupplierId);
			if(trackedSupplier == null)
			{
				_dbContext.Supplier.Add(supplier);
			}

			_dbContext.SaveChanges();
		}

		public void Update(Supplier supplier)
		{
			_dbContext.Entry(supplier).CurrentValues.SetValues(supplier);
			_dbContext.SaveChanges();
		}
	}
}
