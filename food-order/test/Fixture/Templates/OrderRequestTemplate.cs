using System.Collections.Generic;
using food_order.Entrypoint.Rest.Json;

namespace test.Fixture.Templates
{
    public class OrderRequestTemplate
    {
        public static OrderRequest Get()
        {
            var itemRequest = new ItemRequest("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m);
            return new OrderRequest("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", 
                new List<ItemRequest> { itemRequest });
        }
    }
}
