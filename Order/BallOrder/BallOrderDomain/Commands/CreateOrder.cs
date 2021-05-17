using System;
using System.Collections.Generic;
using BallOrder.Models;

namespace BallOrder.Commands
{
    public class CreateOrder
    {
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public OrderState OrderState { get; set; }
        public List<OrderProduct> OrderProducts { get; set; }
    }
}