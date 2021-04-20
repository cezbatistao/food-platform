namespace food_order.Entrypoint.Rest.Json
{
    public class OrderResponse
    {
        public string uuid { get; set; }
        public string restaurantUuid { get; set; }

        public decimal total { get; set; }

        public OrderResponse(string uuid, string restaurantUuid, decimal total) {
          this.uuid = uuid;
          this.restaurantUuid = restaurantUuid;
          this.total = total;
        }
    }
}
