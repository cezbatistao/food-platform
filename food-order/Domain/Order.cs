using System.Collections.Generic;

namespace food_order.Domain
{
    public class Order
    {
        public long? Id { get; }
        public string Uuid { get; }
        public Restaurant.Restaurant Restaurant { get; }
        public List<OrderItem> Itens { get; }
        public decimal Total { get; }

        public Order(long? id, string uuid, Restaurant.Restaurant restaurant, List<OrderItem> itens, decimal total) 
        {
            this.Id = id;
            this.Uuid = uuid;
            this.Restaurant = restaurant;
            this.Itens = itens;
            this.Total = total;
        }
    }
}
