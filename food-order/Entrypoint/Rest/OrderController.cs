using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

using food_order.Domain;
using food_order.UseCase;
using food_order.Entrypoint.Rest.Json;

namespace food_order.Entrypoint.Rest
{
    [Route("api/")]
    [ApiController]
    public class OrderController : Controller
    {
        private readonly RegisterOrder registerOrder;

        public OrderController(RegisterOrder registerOrder)
        {
            this.registerOrder = registerOrder;
        }

        [HttpPost("v1/orders")]
        public ActionResult<OrderResponse> RegisterOrder([FromBody] OrderRequest orderRequest)
        {
            List<Item> itens = orderRequest.Itens.ConvertAll(itemRequest => 
                new Item(itemRequest.Uuid, itemRequest.Name, itemRequest.Value));
            Order orderToRegister = new Order(orderRequest.RestaurantUuid, itens);

            RequestedOrder requestedOrder = this.registerOrder.Execute(orderToRegister);

            OrderResponse orderResponse = new OrderResponse(requestedOrder.Uuid, 
                requestedOrder.RestaurantUuid, requestedOrder.Total);
            
            return Ok(new DataResponse<OrderResponse>(orderResponse));
        }
    }
}
