using System;

namespace Domain.Commands
{
	public class UpdateStockCommand
	{
		public Guid ProductId { get; set; }
		public StockUpdateType UpdateType { get; set; }
		public int Amount { get; set; }
	}

	public enum StockUpdateType
	{
		Add = 1,
		Remove = 2
	}
}
