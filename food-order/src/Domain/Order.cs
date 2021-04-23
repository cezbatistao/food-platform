using System.Collections.Generic;

namespace food_order.Domain
{
    public class Order
    {
        public long? Id { get; protected set; }
        public string Uuid { get; protected set; }
        public Restaurant.Restaurant Restaurant { get; protected set; }
        public List<OrderItem> Items { get; protected set; }
        public decimal Total { get; protected set; }

        public Order(string uuid, Restaurant.Restaurant restaurant, List<OrderItem> items, decimal total) : 
            this(null, uuid, restaurant, items, total) { }

        public Order(long? id, string uuid, Restaurant.Restaurant restaurant, List<OrderItem> items, decimal total) 
        {
            this.Id = id;
            this.Uuid = uuid;
            this.Restaurant = restaurant;
            this.Items = items;
            this.Total = total;
        }

        protected Order()
        {
            
        }
    }
}
