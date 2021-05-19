using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Primitives;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace InventoryServices.Helpers
{
	public static class HttpContextHelper
	{
		/// <summary>
		/// Try to retrieve an object from http header
		/// </summary>
		/// <typeparam name="T">The type of the object within the header</typeparam>
		/// <param name="headerName">The name of the header</param>
		/// <param name="context">The http context in which to search for the header object</param>
		/// <returns>Whether or not the header was found and an error message string to describe what went wrong</returns>
		public static (bool, string) TryGetHeaderObject<T>(string headerName, HttpContext context, out T headerObject)
		{
			headerObject = default;

			if (!context.Request.Headers.TryGetValue("X-Token", out StringValues value))
				return (false, "Missing X-Token header");

			try
			{
				string json = value.ToString();
				headerObject = JsonConvert.DeserializeObject<T>(json);
			}
			catch(Exception e)
			{
				return (false, e.Message);
			}

			return (true, null);
		}
	}
}
