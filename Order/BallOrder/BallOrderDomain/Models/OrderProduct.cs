using System;
using System.ComponentModel.DataAnnotations;
using BallOrderDomain.Events.Incoming;

namespace BallOrder.Models
{
    public class OrderProduct
    {
        [Key]
        public Guid OrderProductId { get; set; } = new Guid();
        public Order Order { get; set; }
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