using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using Microsoft.EntityFrameworkCore;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderRepository : IOrderRepository
    {
        BallOrderDBContext _dbContext;

        public EFOrderRepository(BallOrderDBContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async void CreateOrderInDatabase(Order order)
        {
            _dbContext.Orders.Add(order);
            await _dbContext.SaveChangesAsync();
        }

        public async Task<List<Order>> GetAllOrders()
        {
            return await _dbContext.Orders.ToListAsync();
        }

        public async Task<Order> GetByCustomerId(Guid orderId)
        {
            return await _dbContext.Orders.FirstOrDefaultAsync(o => o.OrderId == orderId);
        }
    }
}