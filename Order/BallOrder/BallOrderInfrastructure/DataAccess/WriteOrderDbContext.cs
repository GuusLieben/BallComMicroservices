using BallOrder.Models;
using BallOrderDomain.Events;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using Polly;
using System;
using System.Threading.Tasks;

namespace BallOrderInfrastructure.DataAccess
{
    public class WriteOrderDbContext : DbContext
    {
        public WriteOrderDbContext(DbContextOptions<WriteOrderDbContext> options) : base(options)
        {

        }

        public DbSet<EventLog> EventLog { get; set; }

        public async Task SetEventLog(IEvent evt)
        {
            EventLog log = new EventLog()
            {
                EventId = evt.EventId,
                EventName = evt.EventName,
                EventJson = JsonConvert.SerializeObject(evt),
                OrderId = evt.OrderId,
                ProductId = evt.ProductId
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
