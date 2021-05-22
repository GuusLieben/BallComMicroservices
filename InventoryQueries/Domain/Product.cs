using Domain.Events;
using System;

namespace Domain
{
	public class Product
	{
		public Guid ProductId { get; set; }

		public string Name { get; set; }
		public string BarCode { get; set; }
		public int Amount { get; set; }
		public string Description { get; set; }
		public decimal Price { get; set; }
		public string Brand { get; set; }
		public string Supplier { get; set; }

		public Product() { }
		public Product(ProductCreatedEvent pcEvent)
		{
			ProductId = pcEvent.ProductId;
			Name = pcEvent.Name;
			BarCode = pcEvent.BarCode;
			Amount = pcEvent.Amount;
			Description = pcEvent.Description;
			Price = pcEvent.Price;
			Brand = pcEvent.BrandName;
			Supplier = pcEvent.SupplierName;
		}
	}
}
