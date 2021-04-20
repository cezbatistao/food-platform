using System;
using System.Collections.Generic;
using food_order.Domain;
using food_order.Domain.Exception;
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
        public void Should()
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
    }
}