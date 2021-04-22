using System;
using System.Collections.Generic;
using System.Linq;
using FluentValidation;
using food_order.Domain;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway;

namespace food_order.UseCase
{
    public class RegisterOrder
    {
        private readonly IOrderGateway _orderGateway;
        private readonly IRestaurantGateway _restaurantGateway;
        private readonly OrderedValidator _validator;
        
        public RegisterOrder(IOrderGateway orderGateway, 
            IRestaurantGateway restaurantGateway)
        {
            this._orderGateway = orderGateway;
            this._restaurantGateway = restaurantGateway;

            this._validator = new OrderedValidator();
        }
        
        public virtual Order Execute(Ordered ordered)
        {
            _validator.ValidateAndThrow(ordered);
                
            RestaurantDetail restaurantDetail = _restaurantGateway.findById(ordered.restaurantUuid);
            List<MenuItem> items = restaurantDetail.Items;

            bool orderOk = ordered.items.TrueForAll(orderedItem => 
                items.Exists(menuItem => 
                    menuItem.Uuid.Equals(orderedItem.Uuid) && menuItem.Value == orderedItem.UnitValue
                )
            );

            if (orderOk)
            {
                var restaurant = new Restaurant(restaurantDetail.Uuid, restaurantDetail.Name);
                List<OrderItem> orderItems = ordered.items.Select(orderedItem =>
                {
                    MenuItem menuItem = items.Find(menuItem => menuItem.Uuid.Equals(orderedItem.Uuid));
                    return new OrderItem(menuItem.Uuid, menuItem.Name, orderedItem.Amount, menuItem.Value);
                }).ToList();
                var order = new Order(Guid.NewGuid().ToString(), restaurant, orderItems, ordered.total);
                return _orderGateway.register(order);
            }

            throw new InvalidOrderException(
                "0002",
                "invalidOrderException",
                "Order with invalid items");
        }
    }

    class OrderedValidator : AbstractValidator<Ordered>
    {
        public OrderedValidator()
        {
            RuleFor(ordered => ordered.restaurantUuid)
                .Cascade(CascadeMode.Stop)
                .NotNull()
                .NotEmpty();
            RuleFor(ordered => ordered.items)
                .Cascade(CascadeMode.Stop)
                .NotNull()
                .Must(items => items is {Count: > 0}).WithMessage("'items' must not be empty.");
            RuleForEach(ordered => ordered.items).SetValidator(new OrderedItemValidator());
        }
    }

    class OrderedItemValidator : AbstractValidator<OrderedItem>
    {
        public OrderedItemValidator()
        {
            RuleFor(orderedItem => orderedItem.Uuid)
                .Cascade(CascadeMode.Stop)
                .NotNull()
                .NotEmpty();
            RuleFor(orderedItem => orderedItem.Amount)
                .Cascade(CascadeMode.Stop)
                .Must(amount => amount > 0).WithMessage("'amount' must greater than zero.");
            RuleFor(orderedItem => orderedItem.UnitValue)
                .Cascade(CascadeMode.Stop)
                .Must(unitValue => unitValue > 0.0m).WithMessage("'unit Value' must greater than zero.");
        }
    }
}
