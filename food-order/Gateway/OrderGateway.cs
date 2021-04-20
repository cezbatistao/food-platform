using food_order.Domain;

namespace food_order.Gateway
{
    public interface IOrderGateway
    {
        RequestedOrder register(Order Order);
    }
}
