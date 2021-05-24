using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using BallOrderDomain.Events.Incoming;

namespace BallOrder.Models
{
    public class OrderProduct
    {
        [Key]
        public Guid OrderProductId { get; set; } = Guid.NewGuid();

        public Guid OrderId { get; set; }

        [NotMapped]
        public Order Order { get; set; }

        public Guid ProductId { get; set; }
        [NotMapped]
        public Product Product { get; set; }
        public int Amount { get; set; }

        public OrderProduct()
        {

        }
        public OrderProduct(Order order, Basket basket)
        {
            Order = order;
            Product = basket.Product;
            Amount = basket.Amount;
        }
    }
}