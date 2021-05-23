using System;
using System.Collections.Generic;
using System.Text;

namespace PaymentDomain.Events
{
    public interface IEvent
    {
        string EventName { get; }
        Guid EventId { get; set; }
    }
}
