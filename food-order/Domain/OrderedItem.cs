namespace food_order.Domain
{
    public class OrderedItem
    {
        public string Uuid { get; }
        public int Amount { get; }
        public decimal UnitValue { get; }

        public OrderedItem(string uuid, int amount, decimal unitValue) {
            this.Uuid = uuid;
            this.Amount = amount;
            this.UnitValue = unitValue;
        }
    }
}
