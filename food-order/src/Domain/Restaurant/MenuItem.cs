namespace food_order.Domain.Restaurant
{
    public class MenuItem
    {
        public string Uuid { get; protected set; }
        public string Name { get; protected set; }
        public decimal Value { get; protected set; }

        public MenuItem(string uuid, string name, decimal value)
        {
            Uuid = uuid;
            Name = name;
            Value = value;
        }
        
        protected MenuItem() { }
    }
}
