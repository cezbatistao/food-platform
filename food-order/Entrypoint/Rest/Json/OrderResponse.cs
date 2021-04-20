using System.Text.Json.Serialization;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderResponse
    {
        public string Uuid { get; set; }
        [JsonPropertyName("restaurant_uuid")]
        public string RestaurantUuid { get; set; }
        public decimal Total { get; set; }

        public OrderResponse(string uuid, string restaurantUuid, decimal total) {
          this.Uuid = uuid;
          this.RestaurantUuid = restaurantUuid;
          this.Total = total;
        }
    }
}
