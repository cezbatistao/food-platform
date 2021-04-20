namespace food_order.Domain
{
    public class Item
    {
        public string uuid { get; }
        public string name { get; }
        public decimal value { get; }

        public Item(string uuid, string name, decimal value) {
            this.uuid = uuid;
            this.name = name;
            this.value = value;
        }
    }
}
