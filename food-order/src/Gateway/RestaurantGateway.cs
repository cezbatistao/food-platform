using food_order.Domain.Restaurant;

namespace food_order.Gateway
{
    public interface IRestaurantGateway
    {
        RestaurantDetail findById(string uuid);
    }
}