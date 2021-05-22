using Domain.Commands;
using System;

namespace Domain.Events
{
	public class ProductCreatedEvent : IEvent
	{
		public Guid EventId { get; set; } = Guid.NewGuid();
		public Guid OrderId { get; set; }

		public ProductCreatedEvent() { }

		public ProductCreatedEvent(CreateProductCommand command)
		{
			ProductId = command.ProductId;
			Name = command.Name;
			BarCode = command.BarCode;
			Amount = command.Amount;
			Description = command.Description;
			Price = command.Price;
			BrandName = command.Brand;
			SupplierName = command.Supplier ?? "Ball.com";
		}

		public Guid ProductId { get; set; }
		public string Name { get; set; }
		public string BarCode { get; set; }
		public int Amount { get; set; }
		public string Description { get; set; }
		public decimal Price { get; set; }
		public string BrandName { get; set; }
		public string SupplierName { get; set; } = "Ball.com";

		public string EventName => "ProductCreated";
	}
}
