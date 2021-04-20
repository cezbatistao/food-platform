using System.Collections.Generic;

namespace food_order.Domain
{
    public class RequestedOrder
    {
        public long Id { get; }
        public string Uuid { get; }
        public string RestaurantUuid { get; }
        public List<Item> Itens { get; }
        public decimal Total { get; }

        public RequestedOrder(long id, string uuid, string restaurantUuid,
            List<Item> itens, decimal total) 
        {
            this.Id = id;
            this.Uuid = uuid;
            this.RestaurantUuid = restaurantUuid;
            this.Itens = new List<Item>(itens);

            this.Total = total;
        }
    }
}
