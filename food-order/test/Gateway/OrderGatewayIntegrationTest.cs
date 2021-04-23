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
using Xunit;

namespace test.Gateway
{
    public class OrderGatewayIntegrationTest
    {
        private IFixture _fixture;
        
        private readonly IOrderGateway _orderGateway;
        private readonly OrderContext _context;

        public OrderGatewayIntegrationTest()
        {
            // TODO needs embedded database MySQL
            
            /*********************************************************************************/
            // change to IClassFixture: https://hamidmosalla.com/2020/02/02/xunit-part-5-share-test-context-with-iclassfixture-and-icollectionfixture/
            /*********************************************************************************/
            _fixture = new AutoFixture.Fixture().Customize(new GatewayIntegrationTestCustomization());
            
            var serviceProvider = new ServiceCollection()
                .AddEntityFrameworkMySql()
                .BuildServiceProvider();

            var builder = new DbContextOptionsBuilder<OrderContext>();

            var urlConnection = "server=localhost;port=3306;user=order_user;password=order_passwd;database=db_order";
            builder.UseMySql(urlConnection, ServerVersion.AutoDetect(urlConnection))
                .UseInternalServiceProvider(serviceProvider);

            _context = new OrderContext(builder.Options);
            _context.Database.Migrate();

            var mockMapper = new MapperConfiguration(cfg =>
            {
                cfg.AddProfile(new OrderProfile());
            });
            var mapper = mockMapper.CreateMapper();
            /*********************************************************************************/
            
            _context.Database.ExecuteSqlRaw(@"
                DELETE FROM db_order_item;
                DELETE FROM db_order;
            ");

            _orderGateway = new OrderGatewayImpl(_context, mapper);
        }

        [Fact]
        public void ShouldRegisterAOrder()
        {
            // given
            // _context.OrderItemModel.
            //     _context.Database.ExecuteSqlRaw()
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
                },
                orderItem =>
                {
                    Assert.Equal(2, orderItem.Id);
                    Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderItem.Uuid);
                    Assert.Equal("Mussarela", orderItem.Name);
                    Assert.Equal(31.99m, orderItem.UnitValue);
                });
            Assert.Equal(97.97m, orderModel.Total);
        }
    }
}