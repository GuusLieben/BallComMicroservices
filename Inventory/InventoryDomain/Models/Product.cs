using InventoryDomain.Events;
using System;

namespace InventoryDomain.Models
{
	public class Product
	{
		public Guid ProductId { get; set; } = Guid.NewGuid();

		public string Name { get; set; }
		public string BarCode { get; set; }
		public int Amount { get; set; }
		public string Description { get; set; }
		public decimal Price { get; set; }
		public string Brand { get; set; }
		public string Supplier { get; set; }

		public Product(ProductCreated evt)
		{
			ProductId = evt.ProductId;
			Name = evt.Name;
			BarCode = evt.BarCode;
			Amount = evt.Amount;
			Description = evt.Description;
			Price = evt.Price;
			Brand = evt.BrandName;
			Supplier = evt.SupplierName;
		}

		public Product() { }
	}
}
