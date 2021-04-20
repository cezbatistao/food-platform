namespace food_order.Entrypoint.Rest.Json
{
    public class RestaurantResponse
    {
        public string Uuid { get; set; }
        public string Name { get; set; }
        
        public RestaurantResponse(string uuid, string name) {
            this.Uuid = uuid;
            this.Name = name;
        }
    }
}
