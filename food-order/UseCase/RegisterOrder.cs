using System;
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
        
        public Order Execute(Ordered ordered)
        {
            RestaurantDetail restaurantDetail = _restaurantGateway.findById(ordered.restaurantUuid);
            List<MenuItem> itens = restaurantDetail.Itens;

            bool orderOk = ordered.itens.TrueForAll(orderedItem => 
                itens.Exists(menuItem => 
                    menuItem.Uuid.Equals(orderedItem.Uuid) && menuItem.Value == orderedItem.UnitValue
                )
            );

            if (orderOk)
            {
                var restaurant = new Restaurant(restaurantDetail.Uuid, restaurantDetail.Name);
                List<OrderItem> orderItems = ordered.itens.Select(orderedItem =>
                {
                    MenuItem menuItem = itens.Find(menuItem => menuItem.Uuid.Equals(orderedItem.Uuid));
                    return new OrderItem(menuItem.Uuid, menuItem.Name, orderedItem.Amount, menuItem.Value);
                }).ToList();
                var order = new Order(null, Guid.NewGuid().ToString(), restaurant, orderItems, ordered.total);
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
