using System.Collections.Generic;

namespace food_order.Domain.Restaurant
{
    public class RestaurantDetail
    {
        public string Uuid { get; protected set; }
        public string Name { get; protected set; }
        public string Address { get; protected set; }
        public List<MenuItem> Items { get; protected set; }
        
        public RestaurantDetail(string uuid, string name, 
            string address, List<MenuItem> items)
        {
            Uuid = uuid;
            Name = name;
            Address = address;
            Items = items;
        }

        protected RestaurantDetail() { }
    }
}
