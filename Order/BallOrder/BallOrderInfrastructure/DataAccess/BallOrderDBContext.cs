using System;
using BallOrder.Models;
using Microsoft.EntityFrameworkCore;
using Polly;

namespace BallOrderInfrastructure.DataAccess
{
    public class BallOrderDBContext : DbContext
    {
        public BallOrderDBContext(DbContextOptions<BallOrderDBContext> options) : base(options)
        {

        }

        public DbSet<Product> Products { get; set; }
        public DbSet<Order> Orders { get; set; }
        public DbSet<OrderProduct> OrderProducts { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            builder.Entity<Order>().Property(order => order.OrderId).ValueGeneratedOnAdd();
            builder.Entity<Product>().HasKey(product => product.ProductId);
            builder.Entity<OrderProduct>().HasKey(orderProduct => orderProduct.OrderProductId);

            base.OnModelCreating(builder);
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
