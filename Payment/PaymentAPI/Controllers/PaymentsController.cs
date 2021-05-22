using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using PaymentDomain.Events;
using PaymentDomain.Models;
using PaymentDomain.Services;
using PaymentInfrastructure.Context;
using PaymentInfrastructure.Interfaces;

namespace PaymentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentsController : ControllerBase
    {
        private readonly IMessagePublisher _messagePublisher;
        private readonly IPaymentRepository _repository;
        private readonly EventLogContext _eventLogDb;

        public PaymentsController(IMessagePublisher publisher, IPaymentRepository repository, EventLogContext context)
        {
            _messagePublisher = publisher;
            _repository = repository;
            _eventLogDb = context;
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
            PaymentApproved evt = new PaymentApproved(payment);
            await _messagePublisher.PublishMessageAsync(evt);
            await _eventLogDb.LogEvent(evt);
            
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
            PaymentRejected evt = new PaymentRejected(payment);
            await _messagePublisher.PublishMessageAsync(evt);
            await  _eventLogDb.LogEvent(evt);

            return Ok();
        }

        // PUT api/<PaymentsController>/5
        [HttpPut("{id}/pay")]
        public async Task<ActionResult> Recieved(Guid id)
        {
            // change status in database
            Payment payment = _repository.Get(id);
            payment.PaymentState = "Recieved";
            payment.PaymentRecievedDate = new DateTime();
            _repository.Update(payment);

            // publish message
            PaymentRecieved evt = new PaymentRecieved(payment);
            await _messagePublisher.PublishMessageAsync(evt);
            await _eventLogDb.LogEvent(evt);
            
            return Ok();
        }
    }
}
