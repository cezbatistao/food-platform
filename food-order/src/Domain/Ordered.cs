using System.Collections.Generic;
using System.Linq;

namespace food_order.Domain
{
    public class Ordered
    {
        public string RestaurantUuid { get; }
        public List<OrderedItem> Items { get; }
        public decimal Total { get; }

        public Ordered(string restaurantUuid, List<OrderedItem> items) 
        {
            this.RestaurantUuid = restaurantUuid;
            this.Items = items;

            this.Total = items?.Aggregate(0.0m, (acc, item) => acc + (item.Amount * item.UnitValue)) ?? 0.0m;
        }
    }
}
