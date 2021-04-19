using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json
{
    public class ItemRequest
    {
        public string uuid { get; set; }
        public string name { get; set; }
        public double value { get; set; }
    }
}
