using System;
using food_order.Domain;

namespace food_order.Gateway.Database
{
    public class OrderGatewayImpl: IOrderGateway
    {
        public RequestedOrder register(Order Order)
        {
            Random random = new Random();
            return new RequestedOrder(random.Next(), 
                Guid.NewGuid().ToString(), 
                Order.restaurantUuid, 
                Order.itens, 
                Order.total);
        }
    }
}
