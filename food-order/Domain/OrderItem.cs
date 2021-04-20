namespace food_order.Domain
{
    public class OrderItem
    {
        public string Uuid { get; }
        public string Name { get; }
        public int Amount { get; }
        public decimal UnitValue { get; }

        public OrderItem(string uuid, string name, int amount, decimal unitValue) {
            this.Uuid = uuid;
            this.Name = name;
            this.Amount = amount;
            this.UnitValue = unitValue;
        }
    }
}
