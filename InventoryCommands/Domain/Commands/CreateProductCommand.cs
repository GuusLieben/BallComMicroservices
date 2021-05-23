using System;

namespace Domain.Commands
{
	public class CreateProductCommand
	{
		public Guid ProductId { get; set; } = Guid.NewGuid();

		public string Name { get; set; }
		public string BarCode { get; set; }
		public int Amount { get; set; }
		public string Description { get; set; }
		public decimal Price { get; set; }
		public string Brand { get; set; }
		public string Supplier { get; set; }
		public decimal Weight { get; set; }
	}
}
