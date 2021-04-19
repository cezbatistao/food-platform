using System.Collections.Generic;
using System.Linq;

namespace food_order.Domain
{
    public class Order
    {
        public long? id { get; }
        public string uuid { get; }
        public string restaurantUuid { get; }
        public List<Item> itens { get; }

        public double total { get; }

        public Order(long? id, string uuid, string restaurantUuid, List<Item> itens) {
            this.id = id;
            this.uuid = uuid;
            this.restaurantUuid = restaurantUuid;
            this.itens = new List<Item>(itens);

            this.total = itens.Aggregate(0.0, (double acc, Item value) => acc + value.value);
        }

        public Order(string restaurantUuid, List<Item> itens) : this(null, null, restaurantUuid, itens) { }
    }
}
