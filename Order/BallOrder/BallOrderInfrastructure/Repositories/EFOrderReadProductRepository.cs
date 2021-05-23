using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderReadProductRepository : IOrderReadProductRepository
    {
        ReadOrderDbContext _dbContext;

        public EFOrderReadProductRepository(ReadOrderDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task AddProduct(Product product)
        {
            await _dbContext.Products.AddAsync(product);
            await _dbContext.SaveChangesAsync();
        }

        public async Task<IEnumerable<Product>> GetAllProducts()
        {
            return await _dbContext.Products.ToListAsync();
        }

        public async Task<Product> GetProductById(Guid productId)
        {
            return await _dbContext.Products.FindAsync(productId);
        }

        public async Task AddAmount(Guid productId, int amount)
        {
            Product product = await GetProductById(productId);
            if (product != null)
            {
                product.Amount += amount;
                await _dbContext.SaveChangesAsync();
            }
        }

        public async Task RemoveAmount(Guid productId, int amount)
        {
            Product p = await GetProductById(productId);
            if (p != null)
            {
                p.Amount -= amount;
                await _dbContext.SaveChangesAsync();
            }
        }
    }
}