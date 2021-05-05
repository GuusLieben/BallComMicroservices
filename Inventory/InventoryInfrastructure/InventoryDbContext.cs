using InventoryDomain.Models;
using Microsoft.EntityFrameworkCore;
using Polly;
using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryInfrastructure
{
	public class InventoryDbContext : DbContext
	{
		public InventoryDbContext(DbContextOptions<InventoryDbContext> options) : base(options) { }

		public DbSet<Supplier> Supplier { get; set; }
		public DbSet<Brand> Brand { get; set; }

		public void MigrateDB()
		{
			Policy
				.Handle<Exception>()
				.WaitAndRetry(10, r => TimeSpan.FromSeconds(10))
				.Execute(() => Database.Migrate());
		}
	}
}
