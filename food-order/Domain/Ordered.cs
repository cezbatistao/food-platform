using System.Collections.Generic;
using System.Linq;

namespace food_order.Domain
{
    public class Ordered
    {
        public string restaurantUuid { get; }
        public List<OrderedItem> itens { get; }
        public decimal total { get; }

        public Ordered(string restaurantUuid, List<OrderedItem> itens) 
        {
            this.restaurantUuid = restaurantUuid;
            this.itens = new List<OrderedItem>(itens);

            this.total = itens.Aggregate(0.0m, 
                (acc, item) => acc + (item.Amount * item.UnitValue));
        }
    }
}
