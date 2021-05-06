using System;
using System.ComponentModel.DataAnnotations;

namespace food_order.Gateway.Database.Model
{
    public class OrderItemModel
    {
        public long? Id { get; set; }
        public string Uuid { get; set; }
        public string Name { get; set; }
        public int Amount { get; set; }
        public decimal UnitValue { get; set; }
        public long? OrderId { get; set; }
        
        [Timestamp]
        public DateTime CreatedAt { get; set; }
        
        [Timestamp]
        public DateTime LastUpdated { get; set; }
    }
}
