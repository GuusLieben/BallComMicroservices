using System;

namespace BallOrderDomain.Events.Incoming
{
    public class ProductCreated : IEvent
    {
        public Guid EventId { get; set; }
        public Guid ProductId { get; set; }
        public Guid OrderId { get; set; }
        public string Name { get; set; }
        public string BarCode { get; set; }
        public int Amount { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public string BrandName { get; set; }
        public string SupplierName { get; set; }
        public decimal Weight { get; set; }
        public string EventName => "ProductCreated";
    }
}