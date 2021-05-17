using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using Microsoft.EntityFrameworkCore;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderProductRepository : IOrderProductRepository
    {
        BallOrderDBContext _dbContext;

        public EFOrderProductRepository(BallOrderDBContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async void CreateOrderProductInDatabase(OrderProduct orderProduct)
        {
            _dbContext.OrderProducts.Add(orderProduct);
            await _dbContext.SaveChangesAsync();
        }

        public async Task<List<OrderProduct>> GetAllOrderProducts()
        {
            return await _dbContext.OrderProducts.ToListAsync();
        }

        public async Task<OrderProduct> GetByCustomerId(Guid orderProductId)
        {
            return await _dbContext.OrderProducts.FirstOrDefaultAsync(op => op.OrderProductId == orderProductId);
        }
    }
}