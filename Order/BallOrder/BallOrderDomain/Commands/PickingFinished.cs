using BallOrder.Models;
using System;
using System.Collections.Generic;

namespace BallOrderDomain.Commands
{
    public class PickingFinished
    {
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public OrderState OrderState { get; set; }
        public IEnumerable<Basket> Basket { get; set; }
    }
}