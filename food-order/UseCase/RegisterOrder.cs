using System;
using food_order.Domain;

namespace food_order.UseCase
{
    public class RegisterOrder
    {
        public Order Execute(Order order) {
            Random random = new Random();
            return new Order(random.Next(), System.Guid.NewGuid().ToString(), order.restaurantUuid, order.itens);
        }
    }
}
