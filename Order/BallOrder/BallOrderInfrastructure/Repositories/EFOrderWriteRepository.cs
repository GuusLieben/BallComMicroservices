using BallOrderDomain.Events;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using System.Threading.Tasks;

namespace BallOrderInfrastructure.Repositories
{
    public class EFOrderWriteRepository : IOrderWriteRepository
    {
        WriteOrderDbContext _dbContext;

        public EFOrderWriteRepository(WriteOrderDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task AddEventLog(IEvent evt)
        {
            await _dbContext.SetEventLog(evt);
        }
    }
}