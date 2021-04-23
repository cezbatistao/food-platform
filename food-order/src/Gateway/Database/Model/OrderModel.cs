using System.Collections.Generic;

namespace food_order.Gateway.Database.Model
{
    public class OrderModel
    {
        public long? Id { get; set; }
        public string Uuid { get; set; }
        public string RestaurantUuid { get; set; }
        public List<OrderItemModel> Items { get; set; }
        public decimal Total { get; set; }
    }
}