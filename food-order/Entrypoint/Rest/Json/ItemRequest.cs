using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace food_order.Entrypoint.Rest.Json
{
    public class ItemRequest
    {
        public string Uuid { get; set; }
        public int Amount { get; set; }
        [JsonPropertyName("unit_value")]
        public decimal UnitValue { get; set; }
    }
}
