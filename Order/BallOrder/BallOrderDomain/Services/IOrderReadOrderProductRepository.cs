using System.Threading.Tasks;
using BallOrder.Models;

namespace BallOrderDomain.Services
{
    public interface IOrderReadOrderProductRepository
    {
        Task AddOrderProduct(OrderProduct orderProduct);
    }
}