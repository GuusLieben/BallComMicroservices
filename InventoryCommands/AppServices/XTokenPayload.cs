using System;
using System.Collections.Generic;

namespace AppServices
{
	public class XTokenPayload
	{
		public Guid Guid { get; set; }
		public string Email { get; set; }
		public Dictionary<string, object> Meta { get; set; }
	}
}
