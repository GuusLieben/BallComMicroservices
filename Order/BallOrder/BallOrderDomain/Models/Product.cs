using System;
using System.ComponentModel.DataAnnotations;
using BallOrderDomain.Events.Incoming;

namespace BallOrder.Models
{
    public class Product
    {
        [Key]
        public Guid ProductId { get; set; }
        public String Name { get; set; }
        public String Brand { get; set; }
        public String Barcode { get; set; }
        public int Amount { get; set; }

        public Product()
        {

        }
        public Product(ProductCreated command)
        {
            ProductId = command.ProductId;
            Name = command.Name;
            Brand = command.BrandName;
            Barcode = command.BarCode;
            Amount = command.Amount;
        }
    }
}