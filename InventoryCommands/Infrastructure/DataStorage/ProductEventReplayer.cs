using Domain.Events;
using Domain.Models;
using Infrastructure.DataStorage;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Infrastructure.Services
{
	public class ProductEventReplayer
	{
		private readonly EventStoreDbContext _db;

		public ProductEventReplayer(EventStoreDbContext dbContext)
		{
			_db = dbContext;
		}

		/// <summary>
		/// Replays all events associated with the product up until the specified date
		/// </summary>
		public (bool, int) GetProductAmountOn(DateTime utcTime, Guid productId, string supplierName = null)
		{
			IEnumerable<IEvent> events = _db.EventLog
				.Where(evt => evt.CreatedOnDate <= utcTime && evt.ProductId.Equals(productId))
				.OrderBy(evt => evt.CreatedOnDate)
				.Select(evt => DeserializeToEvent(evt));

			bool productExists = false;
			int amount = 0;
			foreach(IEvent evt in events)
			{
				if (evt == null)
					continue;

				switch (evt.EventName)
				{
					case "ProductCreated":
						ProductCreatedEvent pcEvent = evt as ProductCreatedEvent;
						if (supplierName != null && !pcEvent.SupplierName.Equals(supplierName))
							break;

						productExists = true;
						amount += pcEvent.Amount;
						break;

					case "StockAdded":
						StockAddedEvent saEvent = evt as StockAddedEvent;
						amount += saEvent.Amount;

						break;

					case "StockRemoved":
						StockRemovedEvent srEvent = evt as StockRemovedEvent;
						amount -= srEvent.Amount;

						break;
				}
			}

			return (productExists, amount);
		}

		private static IEvent DeserializeToEvent(EventLog log)
		{
			return log.EventName switch
			{
				"ProductCreated" => JsonConvert.DeserializeObject<ProductCreatedEvent>(log.EventJson),
				"StockAdded" => JsonConvert.DeserializeObject<StockAddedEvent>(log.EventJson),
				"StockRemoved" => JsonConvert.DeserializeObject<StockRemovedEvent>(log.EventJson),
				_ => null,
			};
		}
	}
}
