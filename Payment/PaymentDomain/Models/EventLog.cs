using System;
using System.Collections.Generic;
using System.Text;

namespace PaymentDomain.Models
{
    public class EventLog
    {
        public Guid Id { get; set; }
        public DateTime CreatedOnDate { get; set; } = DateTime.UtcNow;
        public string EventName { get; set; }
        public string EventJson { get; set; }
    }
}
