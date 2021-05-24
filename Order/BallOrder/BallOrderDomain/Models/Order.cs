using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using BallOrderDomain.Events.Incoming;

namespace BallOrder.Models
{
    public class Order
    {
        [Key]
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public OrderState OrderState { get; set; }
        public IEnumerable<OrderProduct> OrderProducts { get; set; }

        public Order()
        {

        }
        public Order(OrderCreated command)
        {
            OrderId = command.OrderId;
            CustomerId = command.CustomerId;
            OrderState = OrderState.InitialOrder;
        }
    }
}