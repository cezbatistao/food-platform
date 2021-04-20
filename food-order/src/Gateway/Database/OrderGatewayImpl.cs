using System;
using food_order.Domain;

namespace food_order.Gateway.Database
{
    public class OrderGatewayImpl: IOrderGateway
    {
        public Order register(Order order)
        {
            Random random = new Random();

            return new Order(random.Next(), order.Uuid, order.Restaurant, order.Items, order.Total);
        }
    }
}
