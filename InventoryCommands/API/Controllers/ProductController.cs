using AppServices;
using Domain.Commands;
using Domain.Events;
using Infrastructure.DataStorage;
using Infrastructure.RabbitMQ.Interfaces;
using Infrastructure.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class ProductController : ControllerBase
	{
		private readonly ProductEventReplayer _productReplayer;
		private readonly IMessagePublisher _messagePublisher;
		private readonly EventStoreDbContext _db;

		public ProductController(EventStoreDbContext dbContext, IMessagePublisher publisher, ProductEventReplayer replayer)
		{
			_db = dbContext;
			_productReplayer = replayer;
			_messagePublisher = publisher;
		}

		[HttpPost("internal")]
		public async Task<IActionResult> AddInternalProduct(CreateProductCommand product)
		{
			ProductCreatedEvent evt = new ProductCreatedEvent(product);

			await _messagePublisher.PublishMessageAsync(evt);
			await _db.LogEvent(evt);

			return Ok(product);
		}

		[HttpPost("supplier")]
		public async Task<IActionResult> AddSupplierProduct(CreateProductCommand product)
		{
			(bool success, string err) = HttpContextHelper.TryGetHeaderObject("X-Token-Payload", HttpContext, out XTokenPayload payload);

			if (!success)
				return StatusCode(500, err);

			if (string.IsNullOrEmpty(payload.Company))
				return StatusCode(500, "No company name found in supplier meta data");

			product.Supplier = nameObj.ToString();

			ProductCreatedEvent evt = new ProductCreatedEvent(product);

			await _messagePublisher.PublishMessageAsync(evt);
			await _db.LogEvent(evt);

			return Ok(product);
		}

		[HttpPut("internal/stock/{productId:guid}")]
		public async Task<IActionResult> UpdateInternalStock(Guid productId, UpdateStockCommand command)
		{
			(bool productFound, int currentAmount) = _productReplayer.GetProductAmountOn(DateTime.UtcNow, productId);
			if (!productFound)
				return UnprocessableEntity($"Could not find a product with ID '{productId}'");

			return command.UpdateType switch
			{
				StockUpdateType.Add => await AddStock(productId, command.Amount),
				StockUpdateType.Remove => await RemoveStock(productId, command.Amount, currentAmount),
				_ => BadRequest($"Unknown update type '{command.UpdateType}'")
			};
		}

		[HttpPut("supplier/stock/{productId:guid}")]
		public async Task<IActionResult> UpdateSupplierStock(Guid productId, UpdateStockCommand command)
		{
			(bool success, string err) = HttpContextHelper.TryGetHeaderObject("X-Token-Payload", HttpContext, out XTokenPayload payload);

			if (!success)
				return StatusCode(500, err);

			if(string.IsNullOrEmpty(payload.Company))
				return StatusCode(500, "No company name found in supplier meta data");

			(bool productFound, int currentAmount) = _productReplayer.GetProductAmountOn(DateTime.UtcNow, productId, nameObj.ToString());
			if (!productFound)
				return UnprocessableEntity($"Could not find a product with ID '{productId}'");

			return command.UpdateType switch
			{
				StockUpdateType.Add => await AddStock(productId, command.Amount),
				StockUpdateType.Remove => await RemoveStock(productId, command.Amount, currentAmount),
				_ => BadRequest($"Unknown update type '{command.UpdateType}'")
			};
		}

		private async Task<IActionResult> AddStock(Guid productId, int amount)
		{
			StockAddedEvent evt = new StockAddedEvent()
			{
				ProductId = productId,
				Amount = amount
			};

			await _messagePublisher.PublishMessageAsync(evt);
			await _db.LogEvent(evt);

			return Ok(evt);
		}

		private async Task<IActionResult> RemoveStock(Guid productId, int amount, int currentAmount)
		{
			if (amount > currentAmount)
				return UnprocessableEntity($"Trying to remove more than the current available amount for product with ID '{productId}'");
		
			StockRemovedEvent evt = new StockRemovedEvent()
			{
				ProductId = productId,
				Amount = amount
			};

			await _messagePublisher.PublishMessageAsync(evt);
			await _db.LogEvent(evt);

			return Ok(evt);
		}
	}
}
