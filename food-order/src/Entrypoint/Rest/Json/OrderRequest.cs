using System.Collections.Generic;
using System.Text.Json.Serialization;
using FluentValidation;

namespace food_order.Entrypoint.Rest.Json
{
    public class OrderRequest
    {
        [JsonPropertyName("restaurant_uuid")]
        public string RestaurantUuid { get; set; }
        public List<ItemRequest> Items { get; set; }

        public OrderRequest(string restaurantUuid, List<ItemRequest> items)
        {
            RestaurantUuid = restaurantUuid;
            Items = items;
        }
    }
    
    public class OrderRequestValidator : AbstractValidator<OrderRequest>
    {
        public OrderRequestValidator()
        {
            RuleFor(orderRequest => orderRequest.RestaurantUuid)
                .NotNull()
                .NotEmpty();
            RuleFor(orderRequest => orderRequest.Items)
                .NotNull()
                .NotEmpty();
        }
    }
}
