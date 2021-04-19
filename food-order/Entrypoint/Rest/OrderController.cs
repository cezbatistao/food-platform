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
            List<Item> itens = orderRequest.itens.ConvertAll(itemRequest => 
                new Item(itemRequest.uuid, itemRequest.name, itemRequest.value));
            Order orderToRegister = new Order(orderRequest.restaurantUuid, itens);

            Order registeredOrder = this.registerOrder.Execute(orderToRegister);

            OrderResponse orderResponse = new OrderResponse(registeredOrder.uuid, registeredOrder.restaurantUuid, registeredOrder.total);
            
            return Ok(orderResponse);
        }
    }
}