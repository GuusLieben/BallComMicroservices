using Domain;
using Microsoft.EntityFrameworkCore;
using Polly;
using System;

namespace Infrastructure.DataStorage
{
	public class ProductDbContext : DbContext
	{
		public ProductDbContext(DbContextOptions<ProductDbContext> options) : base(options) { }

		public DbSet<Product> Product { get; set; }

		public void MigrateDB()
		{
			Policy
				.Handle<Exception>()
				.WaitAndRetry(10, r => TimeSpan.FromSeconds(10))
				.Execute(() => Database.Migrate());
		}
	}
}
