using System.Linq;
using AutoFixture;
using AutoMapper;
using food_order.Domain;
using food_order.Gateway;
using food_order.Gateway.Database;
using food_order.Gateway.Database.Data;
using food_order.Gateway.Database.Model;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using test.Fixture;
using test.Support;
using Xunit;

namespace test.Gateway
{
    public class OrderGatewayIntegrationTest : IClassFixture<DatabaseFixture>
    {
        private IFixture _fixture;

        private DatabaseFixture _databaseFixture;
        
        private readonly IOrderGateway _orderGateway;
        private readonly OrderContext _context;

        public OrderGatewayIntegrationTest(DatabaseFixture databaseFixture)
        {
            _databaseFixture = databaseFixture;
            _fixture = new AutoFixture.Fixture().Customize(new GatewayIntegrationTestCustomization());
            
            _context = _databaseFixture.OrderContext;
            
            _context.Database.ExecuteSqlRaw(@"
                DELETE FROM db_order_item;
                DELETE FROM db_order;
            ");

            _orderGateway = new OrderGatewayImpl(_context, _databaseFixture.Mapper);
        }

        [Fact]
        public void ShouldRegisterAOrder()
        {
            // given
            var registeredOrder = _fixture.Create<Order>();
            
            // when
            Order registerOrder = _orderGateway.register(registeredOrder);
            
            // then 
            Assert.NotNull(registerOrder.Id);
            
            // and
            var orderModel = _context.OrderModel
                .First(orderModel => orderModel.Id == registeredOrder.Id);
            
            Assert.NotNull(orderModel);
            Assert.NotNull(orderModel.Id);
            Assert.Equal("9e814a9a-4465-4312-adee-a9d3af86d895", orderModel.Uuid);
            Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", orderModel.RestaurantUuid);
            Assert.Collection(orderModel.Items,
                orderItem =>
                {
                    Assert.Equal(1, orderItem.Id);
                    Assert.Equal("843bfe62-9543-11eb-a8b3-0242ac130003", orderItem.Uuid);
                    Assert.Equal("Pepperoni", orderItem.Name);
                    Assert.Equal(33.99m, orderItem.UnitValue);
                    Assert.NotNull(orderItem.CreatedAt);
                    Assert.NotNull(orderItem.LastUpdated);
                },
                orderItem =>
                {
                    Assert.Equal(2, orderItem.Id);
                    Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderItem.Uuid);
                    Assert.Equal("Mussarela", orderItem.Name);
                    Assert.Equal(31.99m, orderItem.UnitValue);
                    Assert.NotNull(orderItem.CreatedAt);
                    Assert.NotNull(orderItem.LastUpdated);
                });
            Assert.Equal(97.97m, orderModel.Total);
            Assert.NotNull(orderModel.CreatedAt);
            Assert.NotNull(orderModel.LastUpdated);
        }
    }
}