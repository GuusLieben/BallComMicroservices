using System;
using System.ComponentModel.DataAnnotations;

namespace BallOrder.Models
{
    public class EventLog
    {
        [Key]
        public Guid EventId { get; set; } = Guid.NewGuid();
        public DateTime OnDateCreated { get; set; } = DateTime.UtcNow;

        public string EventName { get; set; }
        public string EventJson { get; set; }
        public Guid OrderId { get; set; }
        public Guid ProductId { get; set; }
    }
}