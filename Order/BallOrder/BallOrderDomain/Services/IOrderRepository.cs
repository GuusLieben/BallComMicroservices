using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using BallOrder.Models;

namespace BallOrderDomain.Services
{
    public interface IOrderRepository
    {
        void CreateOrderInDatabase(Order order);

        Task<List<Order>> GetAllOrders();

        Task<Order> GetByCustomerId(Guid orderId);
    }
}