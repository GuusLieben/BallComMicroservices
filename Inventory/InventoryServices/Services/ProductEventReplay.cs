using InventoryDomain.Events;
using InventoryDomain.Models;
using InventoryInfrastructure;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;

namespace InventoryServices.Services
{
	public class ProductEventReplay
	{
		private readonly WriteDbContext _writeDb;

		public ProductEventReplay(WriteDbContext dbContext)
		{
			_writeDb = dbContext;
		}

		/// <summary>
		/// Get the current state of products on the specified utc time
		/// </summary>
		public IEnumerable<Product> ReplayUntil(DateTime utcTime, string supplierName = null)
		{
			IEnumerable<IEvent> events = _writeDb.EventLog
				.Where(evt => evt.CreatedOnDate <= utcTime)
				.OrderBy(evt => evt.CreatedOnDate)
				.Select(evt => DeserializeToEvent(evt));

			List<Product> products = new List<Product>();
			foreach(IEvent evt in events)
			{
				Product p;
				switch(evt.EventName)
				{
					case "ProductCreated":
						ProductCreated pcEvent = evt as ProductCreated;
						if (supplierName != null && !pcEvent.SupplierName.Equals(supplierName))
							break;

						products.Add(new Product(pcEvent));
						break;

					case "StockAdded":
						StockAdded saEvent = evt as StockAdded;
						p = products.FirstOrDefault(p => p.ProductId == saEvent.ProductId);
						if (p != null)
							p.Amount += saEvent.Amount;

						break;

					case "StockRemoved":
						StockRemoved srEvent = evt as StockRemoved;
						p = products.FirstOrDefault(p => p.ProductId == srEvent.ProductId);
						if (p != null)
							p.Amount -= srEvent.Amount;

						break;
				}
			}

			return products;
		}

		private static IEvent DeserializeToEvent(EventLog log)
		{
			return log.EventName switch
			{
				"ProductCreated" => JsonConvert.DeserializeObject<ProductCreated>(log.EventJson),
				"StockAdded" => JsonConvert.DeserializeObject<StockAdded>(log.EventJson),
				"StockRemoved" => JsonConvert.DeserializeObject<StockRemoved>(log.EventJson),
				_ => null,
			};
		}
	}
}
