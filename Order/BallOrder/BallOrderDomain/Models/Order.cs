using System;

namespace BallOrder.Models
{
    public class Order
    {
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public OrderState OrderState { get; set; }
    }
}