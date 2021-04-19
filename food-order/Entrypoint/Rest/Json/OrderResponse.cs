namespace food_order.Entrypoint.Rest.Json
{
    public class OrderResponse
    {
        public string uuid { get; set; }
        public string restaurantUuid { get; set; }

        public double total { get; set; }

        public OrderResponse(string uuid, string restaurantUuid, double total) {
          this.uuid = uuid;
          this.restaurantUuid = restaurantUuid;
          this.total = total;
        }
    }
}
