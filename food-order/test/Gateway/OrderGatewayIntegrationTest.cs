using AutoFixture;
using AutoMapper;
using food_order.Domain;
using food_order.Gateway;
using food_order.Gateway.Database;
using food_order.Gateway.Database.Data;
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

        public OrderGatewayIntegrationTest()
        {
            _fixture = new AutoFixture.Fixture().Customize(new GatewayIntegrationTestCustomization());
            
            var serviceProvider = new ServiceCollection()
                .AddEntityFrameworkMySql()
                .BuildServiceProvider();

            var builder = new DbContextOptionsBuilder<OrderContext>();

            var urlConnection = "server=localhost;port=3306;user=order_user;password=order_passwd;database=db_order";
            builder.UseMySql(urlConnection, ServerVersion.AutoDetect(urlConnection))
                .UseInternalServiceProvider(serviceProvider);

            var context = new OrderContext(builder.Options);
            // context.Database.Migrate();
            
            var mockMapper = new MapperConfiguration(cfg =>
            {
                cfg.AddProfile(new OrderProfile());
            });
            var mapper = mockMapper.CreateMapper();

            _orderGateway = new OrderGatewayImpl(context, mapper);
        }

        [Fact]
        public void ShouldRegisterAOrder()
        {
            // given
            var registeredOrder = _fixture.Create<Order>();
            
            // when
            Order registerOrder = _orderGateway.register(registeredOrder);
            
            // then 
            Assert.NotNull(registerOrder);
        }
        
    }
}