using System.Collections.Generic;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;

namespace food_order.Gateway.Http
{
    public class RestaurantGatewayImpl: IRestaurantGateway
    {
        public Restaurant findById(string Uuid)
        {
            if (Uuid.Equals("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f"))
            {
                List<MenuItem> itens = new List<MenuItem>()
                {
                    new(1, 
                        "743b55f8-9543-11eb-a8b3-0242ac130003", 
                        "Pepperoni", 
                        33.99m
                    ), 
                    new(2, 
                        "773712b0-9543-11eb-a8b3-0242ac130003", 
                        "Meat", 
                        34.99m
                    ), 
                    new(3, 
                        "7d35de8a-9543-11eb-a8b3-0242ac130003", 
                        "Supreme", 
                        35.99m
                    )
                };
            
                return new Restaurant(1, 
                    "cbb9c2bd-abde-48a3-891a-6229fc9b7c2f", 
                    "Pizza Hut", 
                    "Av. Nome da avenida, 123", 
                    itens
                );                
            }
            else
            {
                throw new EntityNotFoundException(
                    "0001", 
                    "entityNotFoundException", 
                    $"Restaurant {Uuid} don't exists"
                );
            }
        }
    }
}