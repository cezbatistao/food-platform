using System;
using System.Collections.Generic;
using food_order.Domain;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway;
using food_order.UseCase;
using Moq;
using Xunit;

namespace test.UseCase
{
    public class UnitTestRegisterOrder
    {
        private RegisterOrder _registerOrder;

        private Mock<IOrderGateway> _mockIOrderGateway;
        private Mock<IRestaurantGateway> _mockIRestaurantGateway;
        
        public UnitTestRegisterOrder()
        {
            _mockIOrderGateway = new Mock<IOrderGateway>();  
            _mockIRestaurantGateway = new Mock<IRestaurantGateway>();
            
            _registerOrder = new RegisterOrder(_mockIOrderGateway.Object, _mockIRestaurantGateway.Object);
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

            List<OrderedItem> items = new List<OrderedItem>
            {
                new ("843bfe62-9543-11eb-a8b3-0242ac130003", 1, 33.99m),
                new ("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m), 
            };
            Ordered ordered = new Ordered(invalidRestaurantUuid, items);
            
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
                "303f58dd-b5a4-496b-ae03-5510c1854acf", 
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
            Assert.Equal("303f58dd-b5a4-496b-ae03-5510c1854acf", order.Uuid);
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
