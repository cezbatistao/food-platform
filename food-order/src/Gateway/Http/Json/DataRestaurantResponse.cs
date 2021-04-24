using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;
using food_order.Gateway.Http.Json.Error;
using System;
using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace food_order.Gateway.Http.Json
{
    public class DataRestaurantResponse
    {
        [JsonPropertyName("data")]
        public RestaurantResponse Restaurant { get; set; }
        
        [JsonPropertyName("error")]
        public ErrorDetailResponse Error { get; set; }

        public DataRestaurantResponse(RestaurantResponse restaurant, ErrorDetailResponse error)
        {
            Restaurant = restaurant;
            Error = error;
        }

        protected DataRestaurantResponse() { }
    }
    
    public class RestaurantResponse
    {
        [JsonPropertyName("uuid")]
        public string Uuid { get; set; }
        
        [JsonPropertyName("name")]
        public string Name { get; set; }
        
        [JsonPropertyName("description")]
        public string Description { get; set; }
        
        [JsonPropertyName("address")]
        public string Address { get; set; }
        
        [JsonPropertyName("itens")]
        public List<MenuItemResponse> Items { get; set; }

        public RestaurantResponse(string uuid, string name, string description, 
            string address, List<MenuItemResponse> items)
        {
            Uuid = uuid;
            Name = name;
            Description = description;
            Address = address;
            Items = items;
        }

        protected RestaurantResponse() { }
    }

    public class MenuItemResponse
    {
        [JsonPropertyName("uuid")]
        public string Uuid { get; set; }
        
        [JsonPropertyName("name")]
        public string Name { get; set; }
        
        [JsonPropertyName("description")]
        public string Description { get; set; }
        
        [JsonConverter(typeof(DecimalJsonConverter))]
        [JsonPropertyName("value")]
        public decimal Value { get; set; }

        public MenuItemResponse(string uuid, string name, string description, decimal value)
        {
            Uuid = uuid;
            Name = name;
            Description = description;
            Value = value;
        }

        protected MenuItemResponse() { }
    }
    
    public class DecimalJsonConverter : JsonConverter<decimal>
    {
        public override decimal Read(
            ref Utf8JsonReader reader,
            Type typeToConvert,
            JsonSerializerOptions options) =>
            Convert.ToDecimal(reader.GetString());

        public override void Write(
            Utf8JsonWriter writer,
            decimal value,
            JsonSerializerOptions options) =>
            writer.WriteStringValue(value.ToString(CultureInfo.InvariantCulture));
    }
}
