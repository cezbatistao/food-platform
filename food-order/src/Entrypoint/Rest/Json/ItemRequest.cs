using System.Collections.Generic;
using System.Text.Json.Serialization;
using FluentValidation;

namespace food_order.Entrypoint.Rest.Json
{
    public class ItemRequest
    {
        public string Uuid { get; set; }
        public int Amount { get; set; }
        
        [JsonPropertyName("unit_value")]
        public decimal UnitValue { get; set; }

        public ItemRequest(string uuid, int amount, decimal unitValue)
        {
            Uuid = uuid;
            Amount = amount;
            UnitValue = unitValue;
        }

        public class ItemRequestValidator : AbstractValidator<ItemRequest>
        {
            public ItemRequestValidator()
            {
                RuleFor(itemRequest => itemRequest.Uuid)
                    .NotNull()
                    .NotEmpty();
                RuleFor(itemRequest => itemRequest.Amount)
                    .GreaterThan(0)
                    .WithMessage("'{PropertyName}' must greater than zero.");
                RuleFor(itemRequest => itemRequest.UnitValue)
                    .GreaterThan(0.0m)
                    .WithMessage("'{PropertyName}' must greater than zero.");
            }
        }
    }
}
