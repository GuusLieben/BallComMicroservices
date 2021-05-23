using BallOrder.Models;
using System;
using System.Collections.Generic;
using BallOrderDomain.Events.Incoming;

namespace BallOrderDomain.Events.Outgoing
{
    public class OrderPutOnHold : IEvent
    {
        public Guid EventId { get; set; } = Guid.NewGuid();
        public Guid OrderId { get; set; }
        public OrderState OrderState { get; set; }
        public Guid ProductId { get; set; }
        public string EventName => "OrderPutOnHold";

        public OrderPutOnHold(OutOfStock command)
        {
            OrderId = command.OrderId;
            OrderState = OrderState.WaitingForStock;
        } 

    }
}