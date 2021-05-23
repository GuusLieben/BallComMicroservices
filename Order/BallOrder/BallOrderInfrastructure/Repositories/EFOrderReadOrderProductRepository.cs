using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderReadOrderProductRepository : IOrderReadOrderProductRepository
    {
        ReadOrderDbContext _dbContext;

        public EFOrderReadOrderProductRepository(ReadOrderDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task AddOrderProduct(OrderProduct orderProduct)
        {
            await _dbContext.OrderProducts.AddAsync(orderProduct);
            await _dbContext.SaveChangesAsync();
        }
    }
}