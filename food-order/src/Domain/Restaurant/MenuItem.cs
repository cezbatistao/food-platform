namespace food_order.Domain.Restaurant
{
    public class MenuItem
    {
        public string Uuid { get; }
        public string Name { get; }
        public decimal Value { get; }

        public MenuItem(string uuid, string name, decimal value)
        {
            Uuid = uuid;
            Name = name;
            Value = value;
        }
    }
}
