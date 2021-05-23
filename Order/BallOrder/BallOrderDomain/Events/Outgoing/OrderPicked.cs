using BallOrder.Models;
using System;
using System.Collections.Generic;
using BallOrderDomain.Commands;

namespace BallOrderDomain.Events.Outgoing
{
    public class OrderPicked : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid OrderId { get; set; }
        public Guid CustomerId { get; set; }
        public OrderState OrderState { get; set; }
        public IEnumerable<Basket> Basket { get; set; }
        public Guid ProductId { get; set; }
        public string EventName => "OrderPicked";

        public OrderPicked(PickingFinished command)
        {
            OrderId = command.OrderId;
            CustomerId = command.CustomerId;
            OrderState = command.OrderState;
            Basket = command.Basket;
        }
    }
}