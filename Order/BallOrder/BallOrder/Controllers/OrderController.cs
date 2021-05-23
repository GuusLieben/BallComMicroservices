using BallOrderInfrastructure.Repositories;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderDomain.Commands;
using BallOrderDomain.Events.Outgoing;
using BallOrderDomain.Services;
using BallOrderInfrastructure.DataAccess;
using BallOrderInfrastructure.RabbitMQ.Interfaces;

namespace BallOrder.Controllers
{
    [Route("/api/[controller]")]
    public class OrderController : Controller
    {
        //IMessagePublisher _messagePublisher;
        private readonly IOrderWriteRepository _orderWriteRepository;
        private readonly OrderEventReplayer _orderEventReplayer;
        private readonly IMessagePublisher _messagePublisher;
        private readonly IOrderReadOrderRepository _orderReadOrderRepository;

        public OrderController(IOrderWriteRepository orderWriteRepository, OrderEventReplayer orderEventReplayer, 
            IMessagePublisher messagePublisher, IOrderReadOrderRepository orderReadOrderRepository)
        {
            _orderWriteRepository = orderWriteRepository;
            _orderEventReplayer = orderEventReplayer;
            _messagePublisher = messagePublisher;
            _orderReadOrderRepository = orderReadOrderRepository;
        }

        [HttpGet("{orderId}")]
        public async Task<IActionResult> GetByCustomerId(Guid orderId)
        {
            return Ok(await _orderReadOrderRepository.GetOrderById(orderId));
        }

        [HttpPut("{orderId}")]
        public async Task<IActionResult> PickingFinished(Guid orderId, PickingFinished command)
        {
            OrderState orderState = _orderEventReplayer.GetOrderStatus(DateTime.UtcNow, orderId);

            if (orderState != OrderState.ReadyForPicking)
            {
                return StatusCode(500, "Order status is not on Ready for Picking");
            }

            OrderPicked orderPicked = new OrderPicked(command);

            await _messagePublisher.PublishMessageAsync(orderPicked);
            await _orderWriteRepository.AddEventLog(orderPicked);

            return Ok(command);
        }
    }
}