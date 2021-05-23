using BallOrderDomain.Events;
using System.Threading.Tasks;

namespace BallOrderDomain.Services
{
    public interface IOrderWriteRepository
    {
        Task AddEventLog(IEvent evt);
    }
}