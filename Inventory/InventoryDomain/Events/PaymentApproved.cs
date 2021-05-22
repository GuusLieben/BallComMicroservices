using System;

namespace InventoryDomain.Events
{
	public class PaymentApproved : IEvent
	{
		public string EventName => "PaymentApproved";
		public Guid EventId { get; set; }


	}
}
