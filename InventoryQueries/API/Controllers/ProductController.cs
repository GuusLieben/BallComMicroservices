using Infrastructure.DataStorage;
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
		private readonly ProductDbContext _db;

		public ProductController(ProductDbContext db)
		{
			_db = db;
		}

		[HttpGet]
		public IActionResult Get()
		{
			return Ok(_db.Product);
		}
	}
}
