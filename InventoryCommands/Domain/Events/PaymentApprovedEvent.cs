using System;

namespace Domain.Events
{
	public class PaymentApprovedEvent : IEvent
	{
		public string EventName => "PaymentApproved";

		public Guid EventId { get; set; }
		public Guid ProductId { get; set; }

		public Guid OrderId { get; set; }
	}
}
