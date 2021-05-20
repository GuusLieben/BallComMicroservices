using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using PaymentDomain.Events;
using PaymentInfrastructure.Interfaces;

namespace PaymentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentsController : ControllerBase
    {
        private readonly IMessagePublisher _messagePublisher;


        public PaymentsController(IMessagePublisher publisher)
        {
            _messagePublisher = publisher;
        }
        // get payment
        // update status

        // GET: api/<PaymentsController>
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

        // GET api/<PaymentsController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        //// POST api/<PaymentsController>
        //[HttpPost]
        //public async Task<ActionResult> Post()
        //{
        //    await _messagePublisher.PublishMessageAsync(new PaymentRegistered());
        //    return Ok();
        //}

        // PUT api/<PaymentsController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<PaymentsController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {

        }
    }
}
