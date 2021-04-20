using System.Collections.Generic;

namespace food_order.Domain.Restaurant
{
    public class RestaurantDetail
    {
        public string Uuid { get; }
        public string Name { get; }
        public string Address { get; }
        public List<MenuItem> Items { get; }
        
        public RestaurantDetail(string uuid, string name, 
            string address, List<MenuItem> items)
        {
            Uuid = uuid;
            Name = name;
            Address = address;
            Items = items;
        }
    }
}
