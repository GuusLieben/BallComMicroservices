using System;

namespace Domain.Events
{
	public class ProductCreatedEvent
	{
		public ProductCreatedEvent() { }

		public Guid ProductId { get; set; }
		public string Name { get; set; }
		public string BarCode { get; set; }
		public int Amount { get; set; }
		public string Description { get; set; }
		public decimal Price { get; set; }
		public string BrandName { get; set; }
		public string SupplierName { get; set; } = "Ball.com";
		public decimal Weight { get; set; }
	}
}
