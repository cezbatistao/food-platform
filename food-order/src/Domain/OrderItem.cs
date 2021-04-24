namespace food_order.Domain
{
    public class OrderItem
    {
        public long? Id { get; }
        public string Uuid { get; }
        public string Name { get; }
        public int Amount { get; }
        public decimal UnitValue { get; }

        public OrderItem(string uuid, string name, int amount, decimal unitValue) : 
            this(null, uuid, name, amount, unitValue) { }
        
        public OrderItem(long? id, string uuid, string name, int amount, decimal unitValue) {
            this.Id = id;
            this.Uuid = uuid;
            this.Name = name;
            this.Amount = amount;
            this.UnitValue = unitValue;
        }

        OrderItem() { }
    }
}
