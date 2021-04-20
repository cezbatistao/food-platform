using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderRequest
    {
        public string RestaurantUuid { get; set; }
        public List<ItemRequest> Itens { get; set; }
    }
}
