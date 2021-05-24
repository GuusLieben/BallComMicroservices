using System;
using System.Linq;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using Microsoft.EntityFrameworkCore;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderReadOrderRepository : IOrderReadOrderRepository
    {
        ReadOrderDbContext _dbContext;

        public EFOrderReadOrderRepository(ReadOrderDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task AddOrder(Order order)
        {
            await _dbContext.Orders.AddAsync(order);
            await _dbContext.SaveChangesAsync();
        }

        public async Task<Order> GetOrderById(Guid orderId)
        {
            Order order = await _dbContext.Orders.FindAsync(orderId);
            return order;
        }

        public async Task UpdateOrderStatus(Guid orderId, OrderState orderState)
        {
            Order order = await GetOrderById(orderId);
            if (order != null)
            {
                order.OrderState = orderState;
                await _dbContext.SaveChangesAsync();
            }

        }
    }
}