using InventoryDomain.Models;
using System;

namespace InventoryDomain.Events
{
	public class ProductCreated : IEvent
	{
		public Guid EventId { get; set; } = Guid.NewGuid();

		public ProductCreated() { }

		public ProductCreated(Product product)
		{
			ProductId = product.ProductId;
			Name = product.Name;
			BarCode = product.BarCode;
			Amount = product.Amount;
			Description = product.Description;
			Price = product.Price;
			BrandName = product.Brand;
			SupplierName = product.Supplier != null ? product.Supplier : "Ball.com"; 
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
