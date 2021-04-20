using System.Collections.Generic;
using System.Linq;
using food_order.Domain;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway;

namespace food_order.UseCase
{
    public class RegisterOrder
    {
        private readonly IOrderGateway _orderGateway;
        private readonly IRestaurantGateway _restaurantGateway;
        
        public RegisterOrder(IOrderGateway orderGateway, 
            IRestaurantGateway restaurantGateway)
        {
            this._orderGateway = orderGateway;
            this._restaurantGateway = restaurantGateway;
        }
        
        public RequestedOrder Execute(Order order)
        {
            Restaurant restaurant = _restaurantGateway.findById(order.restaurantUuid);
            List<MenuItem> itens = restaurant.Itens;

            bool orderOk = order.itens.TrueForAll(orderItem => 
                itens.Exists(menuItem => 
                    menuItem.Uuid.Equals(orderItem.uuid) && menuItem.Value == orderItem.value
                )
            );

            if (orderOk)
            {
                return _orderGateway.register(order);
            }
            
            throw new InvalidOrderException(
                "0002", 
                "invalidOrderException", 
                $"Order with invalid itens"
            );
        }
    }
}
