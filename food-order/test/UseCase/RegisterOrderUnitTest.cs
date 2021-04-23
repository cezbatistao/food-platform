using System;
using System.Collections.Generic;
using System.Linq;
using FluentValidation;
using food_order.Domain;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway;
using food_order.UseCase;
using Moq;
using test.Support;
using Xunit;

namespace test.UseCase
{
    public class RegisterOrderUnitTest
    {
        private RegisterOrder _registerOrder;

        private Mock<IOrderGateway> _mockIOrderGateway;
        private Mock<IRestaurantGateway> _mockIRestaurantGateway;
        
        public RegisterOrderUnitTest()
        {
            _mockIOrderGateway = new Mock<IOrderGateway>();
            _mockIRestaurantGateway = new Mock<IRestaurantGateway>();
            
            _registerOrder = new RegisterOrder(_mockIOrderGateway.Object, _mockIRestaurantGateway.Object);
        }
        
        public static IEnumerable<object[]> Data =>
            new List<object[]>
            {
                new object[] { new Ordered(null, null), new VerifyValidation(2, 
                        new List<string> {"'Restaurant Uuid' must not be empty.", "'Items' must not be empty."})
                },
                new object[] { new Ordered("", new List<OrderedItem>()), new VerifyValidation(2,  
                    new List<string> {"'Restaurant Uuid' must not be empty.", "'Items' must not be empty."})
                },
                new object[] { new Ordered("   ", null), new VerifyValidation(2,
                    new List<string>{"'Restaurant Uuid' must not be empty.", "'Items' must not be empty."})
                },
                new object[] { new Ordered("fasdfdsa", new List<OrderedItem>()), new VerifyValidation(1, 
                    new List<string>{"'Items' must not be empty."})
                },
                new object[] { new Ordered("fasdfdsa", new List<OrderedItem>{new (null, 0, 0.0m)}), 
                    new VerifyValidation(3, new List<string>
                    {
                        "'Uuid' must not be empty.", "'Amount' must greater than zero.", "'Unit Value' must greater than zero."
                    })
                },
            };

        [Theory]
        [MemberData(nameof(Data))]
        public void ShouldVerifyValidation(Ordered ordered, VerifyValidation verifyValidation)
        {
            // when
            Action act = () => _registerOrder.Execute(ordered);
            
            // then
            ValidationException exception = Assert.Throws<ValidationException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal(verifyValidation.TotalErrors, exception.Errors.Count());
            List<string> actualErrorMessages = exception.Errors.Select(error => error.ErrorMessage).ToList();
            Assert.Equal(actualErrorMessages, verifyValidation.MessagesErrors);
        }

        [Fact]
        public void ShouldStopExecuteWhenDontFindRestaurant()
        {
            // given
            string invalidRestaurantUuid = "fsdafdea";

            _mockIRestaurantGateway.Setup(s => 
                s.findById(invalidRestaurantUuid)
            ).Throws(new EntityNotFoundException(
                "0001", "entityNotFoundException", $"Restaurant {invalidRestaurantUuid} don't exists"));
            
            Ordered ordered = new Ordered(invalidRestaurantUuid, 
                new List<OrderedItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", 1, 33.99m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m), 
                });
            
            // when
            Action act = () => _registerOrder.Execute(ordered);
            
            // then
            EntityNotFoundException exception = Assert.Throws<EntityNotFoundException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal("0001", exception.Code);
            Assert.Equal("entityNotFoundException", exception.Error);
            Assert.Equal($"Restaurant {invalidRestaurantUuid} don't exists", exception.Message);
        }
        
        [Fact]
        public void ShouldStopExecuteWhenItemDontHaveSameUuid()
        {
            // given
            
            var restaurantDetail = new RestaurantDetail(
                "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", 
                "Domino's Pizza", 
                "Rua Nome da rua, 654", 
                new List<MenuItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 33.99m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 31.99m)
                });
            
            _mockIRestaurantGateway.Setup(s => 
                s.findById(restaurantDetail.Uuid)
            ).Returns(restaurantDetail);
            
            Ordered ordered = new Ordered(restaurantDetail.Uuid, 
                new List<OrderedItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", 1, 33.99m),
                    new ("b6e72126-923a-4cbf-bcef-507e65826a72", 2, 31.99m)
                });
            
            // when
            Action act = () => _registerOrder.Execute(ordered);
            
            // then
            InvalidOrderException exception = Assert.Throws<InvalidOrderException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal("0002", exception.Code);
            Assert.Equal("invalidOrderException", exception.Error);
            Assert.Equal("Order with invalid items", exception.Message);
        }
        
        [Fact]
        public void ShouldStopExecuteWhenItemDontHaveSameValue()
        {
            // given
            
            var restaurantDetail = new RestaurantDetail(
                "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", 
                "Domino's Pizza", 
                "Rua Nome da rua, 654", 
                new List<MenuItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 33.99m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 31.99m)
                });
            
            _mockIRestaurantGateway.Setup(s => 
                s.findById(restaurantDetail.Uuid)
            ).Returns(restaurantDetail);
            
            Ordered ordered = new Ordered(restaurantDetail.Uuid, 
                new List<OrderedItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", 1, 20.0m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m)
                });
            
            // when
            Action act = () => _registerOrder.Execute(ordered);
            
            // then
            InvalidOrderException exception = Assert.Throws<InvalidOrderException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal("0002", exception.Code);
            Assert.Equal("invalidOrderException", exception.Error);
            Assert.Equal("Order with invalid items", exception.Message);
        }
        
        [Fact]
        public void ShouldGenerateAOrder()
        {
            // given
            var restaurantDetail = new RestaurantDetail(
                "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", 
                "Domino's Pizza", 
                "Rua Nome da rua, 654", 
                new List<MenuItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 33.99m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 31.99m)
                });
            
            _mockIRestaurantGateway.Setup(s => 
                s.findById(restaurantDetail.Uuid)
            ).Returns(restaurantDetail);

            var registeredOrder = new Order(1, 
                "9e814a9a-4465-4312-adee-a9d3af86d895", 
                new Restaurant(restaurantDetail.Uuid, restaurantDetail.Name), 
                new List<OrderItem>
                {
                    new (1, "843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 1, 33.99m),
                    new (2, "88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 2, 31.99m)
                }, 
                97.97m);
            
            _mockIOrderGateway.Setup(s => 
                s.register(It.IsAny<Order>())
            ).Returns(registeredOrder)
                .Callback<Order>(orderVerify =>
                {
                    Assert.Null(orderVerify.Id);
                    Assert.NotNull(orderVerify.Uuid);
                    Assert.NotNull(orderVerify.Restaurant);
                    Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", orderVerify.Restaurant.Uuid);
                    Assert.Equal("Domino's Pizza", orderVerify.Restaurant.Name);
                    Assert.Collection(orderVerify.Items,
                        orderItem =>
                        {
                            Assert.Null(orderItem.Id);
                            Assert.NotNull(orderItem.Uuid);
                            Assert.Equal("Pepperoni", orderItem.Name);
                            Assert.Equal(33.99m, orderItem.UnitValue);
                        },
                        orderItem =>
                        {
                            Assert.Null(orderItem.Id);
                            Assert.NotNull(orderItem.Uuid);
                            Assert.Equal("Mussarela", orderItem.Name);
                            Assert.Equal(31.99m, orderItem.UnitValue);
                        });
                    Assert.Equal(97.97m, orderVerify.Total);
                });
            
            // when
            Ordered ordered = new Ordered(restaurantDetail.Uuid, 
                new List<OrderedItem>
                {
                    new ("843bfe62-9543-11eb-a8b3-0242ac130003", 1, 33.99m),
                    new ("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m)
                });
            
            Order order = _registerOrder.Execute(ordered);

            // then
            Assert.NotNull(order);
            
            Assert.NotNull(order.Id);
            Assert.Equal("9e814a9a-4465-4312-adee-a9d3af86d895", order.Uuid);
            Assert.NotNull(order.Restaurant);
            Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", order.Restaurant.Uuid);
            Assert.Equal("Domino's Pizza", order.Restaurant.Name);
            Assert.Collection(order.Items,
                orderItem =>
                {
                    Assert.Equal(1, orderItem.Id);
                    Assert.Equal("843bfe62-9543-11eb-a8b3-0242ac130003", orderItem.Uuid);
                    Assert.Equal("Pepperoni", orderItem.Name);
                    Assert.Equal(33.99m, orderItem.UnitValue);
                },
                orderItem =>
                {
                    Assert.Equal(2, orderItem.Id);
                    Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderItem.Uuid);
                    Assert.Equal("Mussarela", orderItem.Name);
                    Assert.Equal(31.99m, orderItem.UnitValue);
                });
            Assert.Equal(97.97m, order.Total);
        }
    }
}
