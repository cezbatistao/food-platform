namespace food_order.Entrypoint.Rest.Json
{
    public class OrderItemResponse
    {
        public string Uuid { get; }
        public string Name { get; }
        public int Amount { get; }
        public decimal UnitValue { get; }

        public OrderItemResponse(string uuid, string name, int amount, decimal unitValue) {
            this.Uuid = uuid;
            this.Name = name;
            this.Amount = amount;
            this.UnitValue = unitValue;
        }
    }
}
