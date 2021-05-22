using InventoryInfrastructure;
using InventoryServices.Helpers;
using InventoryServices.Models;
using Microsoft.AspNetCore.Mvc;
using System.Linq;

namespace InventoryAPI.Controllers
{
	[Route("api/product")]
	[ApiController]
	public class ProductQueriesController : ControllerBase
	{
		private readonly ReadDbContext _readDb;

		public ProductQueriesController(ReadDbContext dbContext)
		{
			_readDb = dbContext;
		}

		[HttpGet("internal")]
		public IActionResult GetProducts()
		{
			return Ok(_readDb.Product);
		}

		[HttpGet("supplier")]
		public IActionResult GetSupplierProducts()
		{
			(bool success, string err) = HttpContextHelper.TryGetHeaderObject("X-Token-Payload", HttpContext, out XTokenPayload payload);

			if (!success)
				return StatusCode(500, err);

			if (!payload.Meta.TryGetValue("company", out object nameObj))
				return StatusCode(500, "No company name found in supplier meta data");

			return Ok(_readDb.Product.Where(p => p.Supplier.Equals(nameObj.ToString())));
		}
	}
}
