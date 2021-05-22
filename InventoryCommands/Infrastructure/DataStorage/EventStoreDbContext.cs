using Domain.Events;
using Domain.Models;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using Polly;
using System;
using System.Threading.Tasks;

namespace Infrastructure.DataStorage
{
	public class EventStoreDbContext : DbContext
	{
		public EventStoreDbContext(DbContextOptions<EventStoreDbContext> options) : base(options) { }

		public DbSet<EventLog> EventLog { get; set; }

		public async Task LogEvent(IEvent evt)
		{
			EventLog log = new EventLog()
			{
				EventId = evt.EventId,
				EventName = evt.EventName,
				EventJson = JsonConvert.SerializeObject(evt),
				ProductId = evt.ProductId,
				OrderId = evt.OrderId
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
