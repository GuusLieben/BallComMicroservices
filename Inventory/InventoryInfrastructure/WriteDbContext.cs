using InventoryDomain.Events;
using InventoryDomain.Models;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using Polly;
using System;
using System.Threading.Tasks;

namespace InventoryInfrastructure
{
	public class WriteDbContext : DbContext
	{
		public WriteDbContext(DbContextOptions<ReadDbContext> options) : base(options) { }

		public DbSet<EventLog> EventLog { get; set; }

		public async Task LogEvent(IEvent evt)
		{
			EventLog log = new EventLog()
			{
				EventName = evt.EventName,
				EventJson = JsonConvert.SerializeObject(evt)
			};

			await EventLog.AddAsync(log);
			await SaveChangesAsync();
		}

		public void MigrateDB()
		{
			Policy
				.Handle<Exception>()
				.WaitAndRetry(10, r => TimeSpan.FromSeconds(10))
				.Execute(() => Database.Migrate());
		}
	}
}
