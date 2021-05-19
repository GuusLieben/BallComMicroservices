using InventoryDomain.Commands;
using InventoryDomain.Events;
using InventoryDomain.Models;
using InventoryInfrastructure;
using InventoryInfrastructure.RabbitMQ.Interfaces;
using InventoryServices.Enum;
using InventoryServices.Helpers;
using InventoryServices.Models;
using InventoryServices.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Primitives;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InventoryAPI.Controllers
{
	[Route("api/product")]
	[ApiController]
	public class ProductCommandsController : ControllerBase
	{
		private readonly ProductEventReplay _productReplayer;
		private readonly IMessagePublisher _messagePublisher;
		private readonly WriteDbContext _writeDb;

		public ProductCommandsController(WriteDbContext dbContext, IMessagePublisher publisher, ProductEventReplay replayer)
		{
			_writeDb = dbContext;
			_productReplayer = replayer;
			_messagePublisher = publisher;
		}

		[HttpPost("internal")]
		public async Task<IActionResult> AddInternalProduct(Product product)
		{
			ProductCreated evt = new ProductCreated(product);
		
			await _messagePublisher.PublishMessageAsync(evt);
			await _writeDb.LogEvent(evt);

			return Ok(product);
		}

		[HttpPost("external")]
		public async Task<IActionResult> AddSupplierProduct(Product product)
		{
			(bool success, string err) = HttpContextHelper.TryGetHeaderObject("X-Token-Payload", HttpContext, out XTokenPayload payload);
			
			if(!success)
				return StatusCode(500, err);

			if (!payload.Meta.TryGetValue("company", out object nameObj))
				return StatusCode(500, "No company name found in supplier meta data");

			product.Supplier = nameObj.ToString();

			ProductCreated evt = new ProductCreated(product);

			await _messagePublisher.PublishMessageAsync(evt);
			await _writeDb.LogEvent(evt);

			return Ok(product);
		}

		[HttpPut("internal/stock/{productId:guid}")]
		public async Task<IActionResult> UpdateInternalStock(Guid productId, StockUpdateCommand command)
		{
			IEnumerable<Product> knownProducts = _productReplayer.ReplayUntil(DateTime.UtcNow);

			return command.UpdateType switch
			{
				StockUpdateType.Add => await AddStock(productId, command.Amount, knownProducts),
				StockUpdateType.Remove => await RemoveStock(productId, command.Amount, knownProducts),
				_ => BadRequest($"Unknown update type '{command.UpdateType}'")
			};
		}

		[HttpPut("supplier/stock/{productId:guid}")]
		public async Task<IActionResult> UpdateSupplierStock(Guid productId, StockUpdateCommand command)
		{
			(bool success, string err) = HttpContextHelper.TryGetHeaderObject("X-Token-Payload", HttpContext, out XTokenPayload payload);

			if (!success)
				return StatusCode(500, err);

			if (!payload.Meta.TryGetValue("company", out object nameObj))
				return StatusCode(500, "No company name found in supplier meta data");

			IEnumerable<Product> knownProducts = _productReplayer.ReplayUntil(DateTime.UtcNow, nameObj.ToString());

			return command.UpdateType switch
			{
				StockUpdateType.Add => await AddStock(productId, command.Amount, knownProducts),
				StockUpdateType.Remove => await RemoveStock(productId, command.Amount, knownProducts),
				_ => BadRequest($"Unknown update type '{command.UpdateType}'")
			};
		}

		private async Task<IActionResult> AddStock(Guid productId, int amount, IEnumerable<Product> knownProducts)
		{
			Product knownProduct = knownProducts.FirstOrDefault(p => p.ProductId == productId);

			if (knownProduct == null)
				return BadRequest($"Unknown ProductId '{productId}'");

			StockAdded evt = new StockAdded()
			{
				ProductId = productId,
				Amount = amount
			};

			await _messagePublisher.PublishMessageAsync(evt);
			await _writeDb.LogEvent(evt);

			return Ok(evt);
		}

		private async Task<IActionResult> RemoveStock(Guid productId, int amount, IEnumerable<Product> knownProducts)
		{
			IEnumerable<Product> knownProducts = _productReplayer.ReplayUntil(DateTime.UtcNow);
			Product knownProduct = knownProducts.FirstOrDefault(p => p.ProductId == productId);

			if (knownProduct == null)
				return BadRequest($"Unknown ProductId '{productId}'");

			if (knownProduct.Amount < amount)
				return UnprocessableEntity("Product amount is lower than the amount to remove");

			StockRemoved evt = new StockRemoved()
			{
				ProductId = productId,
				Amount = amount
			};

			await _messagePublisher.PublishMessageAsync(evt);
			await _writeDb.LogEvent(evt);

			return Ok(evt);
		}

	}
}
