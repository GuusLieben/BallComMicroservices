using System;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Events;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using Polly;

namespace BallOrderInfrastructure.DataAccess
{
    public class ReadOrderDbContext : DbContext
    {
        public ReadOrderDbContext(DbContextOptions<ReadOrderDbContext> options) : base(options)
        {

        }

        public DbSet<Product> Products { get; set; }
        public DbSet<Order> Orders { get; set; }
        public DbSet<OrderProduct> OrderProducts { get; set; }

        public void MigrateDB()
        {
            Policy
                .Handle<Exception>()
                .WaitAndRetry(10, r => TimeSpan.FromSeconds(10))
                .Execute(() => Database.Migrate());
        }
    }
}
