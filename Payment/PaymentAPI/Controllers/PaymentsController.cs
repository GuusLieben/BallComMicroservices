using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using PaymentDomain.Events;
using PaymentDomain.Models;
using PaymentDomain.Services;
using PaymentInfrastructure.Interfaces;

namespace PaymentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentsController : ControllerBase
    {
        private readonly IMessagePublisher _messagePublisher;
        private readonly IPaymentRepository _repository;

        public PaymentsController(IMessagePublisher publisher, IPaymentRepository repository)
        {
            _messagePublisher = publisher;
            _repository = repository;
        }

        // PUT api/<PaymentsController>/5
        [HttpPut("{id}/approve")]
        public async Task<ActionResult> Approved(Guid id)
        {
            // change status in database
            Payment payment = _repository.Get(id);
            payment.PaymentState = "Approved";
            _repository.Update(payment);
            // publish message
            await _messagePublisher.PublishMessageAsync(new PaymentApproved());
            return Ok();
        }

        // PUT api/<PaymentsController>/5
        [HttpPut("{id}/reject")]
        public async Task<ActionResult> Rejected(Guid id)
        {
            // change status in database
            Payment payment = _repository.Get(id);
            payment.PaymentState = "Rejected";
            _repository.Update(payment);
            // publish message
            await _messagePublisher.PublishMessageAsync(new PaymentRejected());

            return Ok();
        }

        // PUT api/<PaymentsController>/5
        [HttpPut("{id}/pay")]
        public async Task<ActionResult> Recieved(Guid id)
        {
            // change status in database
            Payment payment = _repository.Get(id);
            payment.PaymentState = "Recieved";
            _repository.Update(payment);
            // publish message
            await _messagePublisher.PublishMessageAsync(new PaymentRecieved());

            return Ok();
        }

        //// POST api/<PaymentsController>
        [HttpPost]
        public async Task<ActionResult> Post()
        {
            await _messagePublisher.PublishMessageAsync(new OrderCreated() { OrderId = Guid.NewGuid(), PaymentType = "Debit", TotalAmount = 19.95 });
            return Ok();
        }
    }
}
