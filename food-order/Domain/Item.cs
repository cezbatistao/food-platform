namespace food_order.Domain
{
    public class Item
    {
        public string uuid { get; }
        public string name { get; }
        public double value { get; }

        public Item(string uuid, string name, double value) {
            this.name = name;
            this.value = value;
        }
    }
}
