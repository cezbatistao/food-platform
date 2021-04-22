using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderRequest
    {
        [JsonPropertyName("restaurant_uuid")]
        public string RestaurantUuid { get; set; }
        public List<ItemRequest> Items { get; set; }

        public OrderRequest(string restaurantUuid, List<ItemRequest> items)
        {
            RestaurantUuid = restaurantUuid;
            Items = items;
        }
    }
}
