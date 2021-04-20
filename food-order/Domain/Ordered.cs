using System.Collections.Generic;
using System.Linq;

namespace food_order.Domain
{
    public class Ordered
    {
        public string restaurantUuid { get; }
        public List<OrderedItem> items { get; }
        public decimal total { get; }

        public Ordered(string restaurantUuid, List<OrderedItem> items) 
        {
            this.restaurantUuid = restaurantUuid;
            this.items = new List<OrderedItem>(items);

            this.total = items.Aggregate(0.0m, 
                (acc, item) => acc + (item.Amount * item.UnitValue));
        }
    }
}
