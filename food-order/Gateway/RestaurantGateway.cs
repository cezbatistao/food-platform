using food_order.Domain;
using food_order.Domain.Restaurant;

namespace food_order.Gateway
{
    public interface IRestaurantGateway
    {
        Restaurant findById(string Uuid);
    }
}