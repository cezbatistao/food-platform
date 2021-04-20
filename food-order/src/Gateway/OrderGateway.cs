using food_order.Domain;

namespace food_order.Gateway
{
    public interface IOrderGateway
    {
        Order register(Order ordered);
    }
}
