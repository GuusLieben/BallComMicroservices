using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using PaymentDomain.Events;
using PaymentDomain.Models;
using Polly;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PaymentInfrastructure.Context
{
    public class EventLogContext : DbContext
    {
        public EventLogContext(DbContextOptions<EventLogContext> options) : base(options) { }

        public DbSet<EventLog> EventLog { get; set; }

		public async Task LogEvent(IEvent evt)
		{
			EventLog log = new EventLog()
			{
				Id = Guid.NewGuid(),
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
