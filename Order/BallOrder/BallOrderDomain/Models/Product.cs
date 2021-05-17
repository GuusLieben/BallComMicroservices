using System;

namespace BallOrder.Models
{
    public class Product
    {
        public Guid ProductId { get; set; }
        public String Name { get; set; }
        public String Supplier { get; set; }
        public String Brand { get; set; }
        public String Barcode { get; set; }
        public int Amount { get; set; }
    }
}