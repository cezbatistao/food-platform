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
        private readonly RegisterOrder _registerOrder;

        public OrderController(RegisterOrder registerOrder)
        {
            this._registerOrder = registerOrder;
        }

        [HttpPost("v1/orders")]
        public ActionResult<OrderResponse> RegisterOrder([FromBody] OrderRequest orderRequest)
        {
            List<OrderedItem> itens = orderRequest.Itens.ConvertAll(itemRequest => 
                new OrderedItem(itemRequest.Uuid, itemRequest.Amount, itemRequest.UnitValue));
            Ordered orderToRegister = new Ordered(orderRequest.RestaurantUuid, itens);

            Order order = _registerOrder.Execute(orderToRegister);

            var restaurantResponse = new RestaurantResponse(order.Restaurant.Uuid, order.Restaurant.Name);
            List<OrderItemResponse> orderItemResponses = order.Itens.Select(orderItem => 
                    new OrderItemResponse(orderItem.Uuid, orderItem.Name, orderItem.Amount, orderItem.UnitValue))
                .ToList();
            OrderResponse orderResponse = new OrderResponse(order.Uuid, 
                restaurantResponse, orderItemResponses, order.Total);
            
            return Ok(new DataResponse<OrderResponse>(orderResponse));
        }
    }
}
