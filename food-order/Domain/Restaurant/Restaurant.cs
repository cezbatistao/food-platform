using System.Collections.Generic;

namespace food_order.Domain.Restaurant
{
    public class Restaurant
    {
        public Restaurant(long id, string uuid, string name, 
            string address, List<MenuItem> itens)
        {
            Id = id;
            Uuid = uuid;
            Name = name;
            Address = address;
            Itens = itens;
        }

        public long Id { get; }
        public string Uuid { get; }
        public string Name { get; }
        public string Address { get; }
        public List<MenuItem> Itens { get; }
    }
}
