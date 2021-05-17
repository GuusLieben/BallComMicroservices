using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using BallOrder.Models;
using BallOrderInfrastructure.DataAccess;
using BallOrderInfrastructure.RabbitMQ.Interfaces;
using BallOrderInfrastructure.Repositories;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using RabbitMQ.Client;

namespace BallOrder.Controllers
{
    [Route("/api/[controller]")]
    public class OrderController : Controller
    {
        //IMessagePublisher _messagePublisher;
        private readonly EFOrderRepository _orderRepository;
        private readonly IMessagePublisher _messagePublisher;

        public OrderController(EFOrderRepository orderRepository, IMessagePublisher messagePublisher)
        {
            _orderRepository = orderRepository;
            _messagePublisher = messagePublisher;
        }

        [HttpGet]
        public async Task<IActionResult> GetAllOrders()
        {
            return Ok(_orderRepository.GetAllOrders());
        }

        [HttpGet]
        [Route("{orderId}", Name = "GetOrderById")]
        public async Task<IActionResult> GetByCustomerId(Guid orderId)
        {
            var order = _orderRepository.GetByCustomerId(orderId);
            if (order == null)
            {
                return NotFound();
            }
            return Ok(order);
        }

        [HttpPost]
        public async Task<IActionResult> RegisterAsync(Order order)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    // insert customer
                    //Order customer = command.MapToCustomer();

                    _orderRepository.CreateOrderInDatabase(order);

                    // send event

                    await _messagePublisher.PublishMessageAsync("OrderRegistered", order, "Order");


                    // return result
                    return CreatedAtRoute("GetOrderById", new { orderId = order.OrderId }, order);
                }
                return BadRequest();
            }
            catch (DbUpdateException)
            {
                ModelState.AddModelError("", "Unable to save changes. " +
                    "Try again, and if the problem persists " +
                    "see your system administrator.");
                return StatusCode(StatusCodes.Status500InternalServerError);
                throw;
            }
        }
    }
}