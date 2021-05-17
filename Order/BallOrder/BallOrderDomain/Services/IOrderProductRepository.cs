using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using BallOrder.Models;

namespace BallOrderDomain.Services
{
    public interface IOrderProductRepository
    {
        void CreateOrderProductInDatabase(OrderProduct orderProduct);

        Task<List<OrderProduct>> GetAllOrderProducts();

        Task<OrderProduct> GetByCustomerId(Guid orderProductId);
    }
}