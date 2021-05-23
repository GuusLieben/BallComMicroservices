using System;

namespace BallOrderDomain.Events
{
    public interface IEvent
    {
        string EventName { get; }
        Guid EventId { get; set; }
        Guid OrderId { get; set; }
        Guid ProductId { get; set; }
    }
}