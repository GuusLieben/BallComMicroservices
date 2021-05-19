using System;

namespace InventoryDomain.Commands
{
	public class StockUpdateCommand
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
