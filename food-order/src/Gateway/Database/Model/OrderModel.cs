using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace food_order.Gateway.Database.Model
{
    public class OrderModel
    {
        public long? Id { get; set; }
        public string Uuid { get; set; }
        public string RestaurantUuid { get; set; }
        public List<OrderItemModel> Items { get; set; }
        public decimal Total { get; set; }
        
        [Timestamp]
        public DateTime CreatedAt { get; set; }
        
        [Timestamp]
        public DateTime LastUpdated { get; set; }
    }
}
