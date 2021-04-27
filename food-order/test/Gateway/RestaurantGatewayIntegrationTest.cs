using System.Collections.Generic;
using System.Net.Http;
using AutoFixture;
using food_order.Domain.Restaurant;
using food_order.Gateway;
using food_order.Gateway.Database;
using food_order.Gateway.Database.Data;
using food_order.Gateway.Http;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using test.Fixture;
using test.Support;
using Xunit;

namespace test.Gateway
{
    public class RestaurantGatewayIntegrationTest : IClassFixture<MockServerFixture>
    {
        private readonly IFixture _fixture;
        private readonly MockServerFixture _mockServerFixture;
        private readonly IRestaurantGateway _restaurantGateway;

        public RestaurantGatewayIntegrationTest(MockServerFixture mockServerFixture)
        {
            _mockServerFixture = mockServerFixture;
            _fixture = new AutoFixture.Fixture().Customize(new GatewayIntegrationTestCustomization());
            
            string hostname = _mockServerFixture.Hostname;
            ushort port = _mockServerFixture.Port;
            
            var config =
                new ConfigurationBuilder()
                    .AddInMemoryCollection(new List<KeyValuePair<string, string>>
                    {
                        new("RestaurantApp:Url", $"http://{hostname}:{port}")
                    })
                    .Build();
            
            var collection = new ServiceCollection()
                .AddHttpClient("bbc_client")
                .Services
                .BuildServiceProvider();

            var httpClient =  collection.GetService<IHttpClientFactory>().CreateClient("bbc_client");
            
            var restaurantClient = new RestaurantClient(config, httpClient, null);

            _restaurantGateway = new RestaurantGatewayImpl(restaurantClient, _mockServerFixture.Mapper);
        }

        [Fact]
        public void ShouldGetARestaurantDetail()
        {
            // when
            RestaurantDetail restaurantDetail = _restaurantGateway.findById("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f");
            
            // then
            Assert.NotNull(restaurantDetail);
            Assert.Equal("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f", restaurantDetail.Uuid);
            Assert.Equal("Pizza Hut", restaurantDetail.Name);
            Assert.Equal("Av. Nome da avenida, 123", restaurantDetail.Address);
            Assert.Equal(3, restaurantDetail.Items.Count);
            Assert.Collection(restaurantDetail.Items,
                menuItem => 
                {
                    Assert.Equal("743b55f8-9543-11eb-a8b3-0242ac130003", menuItem.Uuid);
                    Assert.Equal("Pepperoni", menuItem.Name);
                    Assert.Equal(33.99m, menuItem.Value);
                }, 
                menuItem =>
                {
                    Assert.Equal("773712b0-9543-11eb-a8b3-0242ac130003", menuItem.Uuid);
                    Assert.Equal("Meat", menuItem.Name);
                    Assert.Equal(34.99m, menuItem.Value);
                }, 
                menuItem =>
                {
                    Assert.Equal("7d35de8a-9543-11eb-a8b3-0242ac130003", menuItem.Uuid);
                    Assert.Equal("Supreme", menuItem.Name);
                    Assert.Equal(35.99m, menuItem.Value);
                });
        }
    }
}