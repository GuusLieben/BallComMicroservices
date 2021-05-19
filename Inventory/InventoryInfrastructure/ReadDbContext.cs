using InventoryDomain.Models;
using Microsoft.EntityFrameworkCore;
using Polly;
using System;

namespace InventoryInfrastructure
{
	public class ReadDbContext : DbContext
	{
		public ReadDbContext(DbContextOptions<ReadDbContext> options) : base(options) { }

		public DbSet<Product> Product { get; set; }
		public DbSet<ProcessedEvent> ProcessedEvent { get; set; }

		public void MigrateDB()
		{
			Policy
				.Handle<Exception>()
				.WaitAndRetry(10, r => TimeSpan.FromSeconds(10))
				.Execute(() => Database.Migrate());
		}
	}
}
