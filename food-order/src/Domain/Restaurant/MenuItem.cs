namespace food_order.Domain.Restaurant
{
    public class MenuItem
    {
        public long Id { get; }
        public string Uuid { get; }
        public string Name { get; }
        public decimal Value { get; }

        public MenuItem(long id, string uuid, string name, decimal value)
        {
            Id = id;
            Uuid = uuid;
            Name = name;
            Value = value;
        }
    }
}
