namespace food_order.Domain.Restaurant
{
    public class Restaurant
    {
        public string Uuid { get; }
        public string Name { get; }

        public Restaurant(string uuid, string name)
        {
            Uuid = uuid;
            Name = name;
        }
    }
}
