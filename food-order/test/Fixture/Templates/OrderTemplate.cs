using System.Collections.Generic;
using food_order.Domain;
using food_order.Domain.Restaurant;

namespace test.Fixture.Templates
{
    public class OrderTemplate
    {
        public static Order GetWithTwoItems()
        {
            return new Order("9e814a9a-4465-4312-adee-a9d3af86d895",
                new Restaurant("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", "Domino's Pizza"),
                new List<OrderItem>
                {
                    new(1, "843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 1, 33.99m),
                    new(2, "88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 2, 31.99m)
                },
                97.97m);
        }
        
        public static Order GetWithSingleItem()
        {
            return new Order(1,
                "27f1fc9a-6bbf-4490-b3e4-42a77059909d",
                new Restaurant("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", "Domino's Pizza"),
                new List<OrderItem>
                {
                    new(2, "88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 2, 31.99m)
                },
                63.98m);
        }
    }
}