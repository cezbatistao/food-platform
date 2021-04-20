using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderResponse
    {
        public string Uuid { get; set; }
        public RestaurantResponse Restaurant { get; set; }
        public List<OrderItemResponse> Items { get; set; }
        public decimal Total { get; set; }

        public OrderResponse(string uuid, RestaurantResponse restaurant, 
            List<OrderItemResponse> items, decimal total) 
        {
          this.Uuid = uuid;
          this.Restaurant = restaurant;
          this.Items = items;
          this.Total = total;
        }
    }
}
