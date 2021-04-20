using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json
{
    public class ItemRequest
    {
        public string Uuid { get; set; }
        public string Name { get; set; }
        public decimal Value { get; set; }
    }
}
