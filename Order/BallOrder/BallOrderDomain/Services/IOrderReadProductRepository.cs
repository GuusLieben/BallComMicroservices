using BallOrder.Models;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BallOrderDomain.Services
{
    public interface IOrderReadProductRepository
    {
        Task AddProduct(Product product);

        Task<IEnumerable<Product>> GetAllProducts();

        Task<Product> GetProductById(Guid productId);
        Task AddAmount(Guid productId, int amount);
        Task RemoveAmount(Guid productId, int amount);
    }
}