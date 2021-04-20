namespace food_order.Domain
{
    public class Item
    {
        public string Uuid { get; }
        public string Name { get; }
        public decimal Value { get; }

        public Item(string uuid, string name, decimal value) {
            this.Uuid = uuid;
            this.Name = name;
            this.Value = value;
        }
    }
}
