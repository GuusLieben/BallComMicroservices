using System;

namespace BallOrder.Models
{
    public class OrderProduct
    {
        public Guid OrderProductId { get; set; }
        public Order Order { get; set; }
        public Product Product { get; set; }
        public int Amount { get; set; }
    }
}