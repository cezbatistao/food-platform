using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderRequest
    {
        public string restaurantUuid { get; set; }
        public List<ItemRequest> itens { get; set; }
    }
}
