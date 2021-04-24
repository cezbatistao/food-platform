using System.Collections.Generic;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;

namespace food_order.Gateway.Http
{
    public class RestaurantGatewayImpl: IRestaurantGateway
    {
        private RestaurantClient _restaurantClient;

        public RestaurantGatewayImpl(RestaurantClient restaurantClient)
        {
            _restaurantClient = restaurantClient;
        }

        public RestaurantDetail findById(string uuid)
        {
            var byUuid = _restaurantClient.GetByUuid(uuid);

            if (uuid.Equals("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f"))
            {
                List<MenuItem> items = new List<MenuItem>()
                {
                    new("743b55f8-9543-11eb-a8b3-0242ac130003", 
                        "Pepperoni", 
                        33.99m
                    ), 
                    new("773712b0-9543-11eb-a8b3-0242ac130003", 
                        "Meat", 
                        34.99m
                    ), 
                    new("7d35de8a-9543-11eb-a8b3-0242ac130003", 
                        "Supreme", 
                        35.99m
                    )
                };
            
                return new RestaurantDetail( 
                    "cbb9c2bd-abde-48a3-891a-6229fc9b7c2f", 
                    "Pizza Hut", 
                    "Av. Nome da avenida, 123", 
                    items
                );                
            }
            else
            {
                throw new EntityNotFoundException(
                    "0001", 
                    "entityNotFoundException", 
                    $"Restaurant {uuid} don't exists"
                );
            }
        }
    }
}
