using System.Collections.Generic;
using System.Linq;

namespace food_order.Domain
{
    public class Order
    {
        public string restaurantUuid { get; }
        public List<Item> itens { get; }
        public decimal total { get; }

        public Order(string restaurantUuid, List<Item> itens) 
        {
            this.restaurantUuid = restaurantUuid;
            this.itens = new List<Item>(itens);

            this.total = itens.Aggregate(0.0m, 
                (decimal acc, Item value) => acc + value.Value);
        }
    }
}
