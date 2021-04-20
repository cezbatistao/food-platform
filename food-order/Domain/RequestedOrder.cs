using System.Collections.Generic;

namespace food_order.Domain
{
    public class RequestedOrder
    {
        public long? id { get; }
        public string uuid { get; }
        public string restaurantUuid { get; }
        public List<Item> itens { get; }
        public decimal total { get; }

        public RequestedOrder(long? id, string uuid, string restaurantUuid,
            List<Item> itens, decimal total) 
        {
            this.id = id;
            this.uuid = uuid;
            this.restaurantUuid = restaurantUuid;
            this.itens = new List<Item>(itens);

            this.total = total;
        }
    }
}
