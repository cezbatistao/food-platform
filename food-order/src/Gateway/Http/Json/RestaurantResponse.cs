using System.Text.Json.Serialization;

namespace food_order.Gateway.Http.Json
{
    public class RestaurantResponse
    {
        [JsonPropertyName("uuid")]
        public string Uuid { get; set; }
        [JsonPropertyName("name")]
        public string Name { get; set; }
        [JsonPropertyName("address")]
        public string Address { get; set; }
        
        public RestaurantResponse(string uuid, string name, string address)
        {
            Uuid = uuid;
            Name = name;
            Address = address;
        }

        protected RestaurantResponse()
        {
        }
    }
}