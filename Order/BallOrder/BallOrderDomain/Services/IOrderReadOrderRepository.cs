using System;
using System.Threading.Tasks;
using BallOrder.Models;

namespace BallOrderDomain.Services
{
    public interface IOrderReadOrderRepository
    {
        Task AddOrder(Order order);
        Task<Order> GetOrderById(Guid orderId);
        Task UpdateOrderStatus(Guid orderId, OrderState orderState);
    }
}