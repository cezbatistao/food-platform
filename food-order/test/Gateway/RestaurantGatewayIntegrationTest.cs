using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway;
using food_order.Gateway.Http;
using food_order.Gateway.Http.Exception;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Moq;
using Moq.Protected;
using test.Support;
using Xunit;

namespace test.Gateway
{
    public class RestaurantGatewayIntegrationTest : IClassFixture<MockServerFixture>
    {
        private IRestaurantGateway _restaurantGateway;
        private IConfiguration _configuration;

        public RestaurantGatewayIntegrationTest(MockServerFixture mockServerFixture)
        {
            string hostname = mockServerFixture.Hostname;
            ushort port = mockServerFixture.Port;
            
            _configuration =
                new ConfigurationBuilder()
                    .AddInMemoryCollection(new List<KeyValuePair<string, string>>
                    {
                        new("RestaurantApp:Url", $"http://{hostname}:{port}")
                    })
                    .Build();
            
            var collection = new ServiceCollection()
                .AddHttpClient("restaurant_client")
                .Services
                .BuildServiceProvider();

            var httpClient =  collection.GetService<IHttpClientFactory>().CreateClient("restaurant_client");
            
            var restaurantClient = new RestaurantClient(_configuration, httpClient, null); // TODO add logger

            _restaurantGateway = new RestaurantGatewayImpl(restaurantClient, mockServerFixture.Mapper);
        }

        [Fact]
        public void ShouldGetARestaurantDetail()
        {
            // given
            var restaurantUuid = "cbb9c2bd-abde-48a3-891a-6229fc9b7c2f";
            
            // when
            RestaurantDetail restaurantDetail = _restaurantGateway.findById(restaurantUuid);
            
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
        
        [Fact]
        public void ShouldThrowAEntityNotFoundExceptionWhenNotFoundRestaurant()
        {
            // given
            var restaurantUuid = "b78bf49d-348e-417d-998e-c1053fceefa6";
            
            // when
            Action act = () => _restaurantGateway.findById(restaurantUuid);

            // then
            EntityNotFoundException exception = Assert.Throws<EntityNotFoundException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal("0001", exception.Code);
            Assert.Equal("entityNotFoundException", exception.Error);
            Assert.Equal($"Restaurant {restaurantUuid} don't exists", exception.Message);
        }
        
        [Fact]
        public void ShouldThrowRequestRestApiExceptionOnRequestWithError()
        {
            // given
            var handler = new Mock<HttpMessageHandler>();
            handler.Protected()
                .Setup<HttpResponseMessage>("Send", ItExpr.IsAny<HttpRequestMessage>(), ItExpr.IsAny<CancellationToken>())
                .Throws(new HttpRequestException("Error on request http"));
            
            var client = new HttpClient(handler.Object, false);
            // var handler = new Mock<HttpMessageHandler>();
            // var client = handler.Object.CreateClient();
            
            var restaurantClient = new RestaurantClient(_configuration, client, null); // TODO add logger
            _restaurantGateway = new RestaurantGatewayImpl(restaurantClient, null);
            
            var restaurantUuid = "b78bf49d-348e-417d-998e-c1053fceefa6";
            
            // when
            Action act = () => _restaurantGateway.findById(restaurantUuid);

            // then
            RequestRestApiException exception = Assert.Throws<RequestRestApiException>(act);
            Assert.NotNull(exception);
            
            Assert.Equal("9998", exception.Code);
            Assert.Equal("requestRestApiException", exception.Error);
            Assert.Equal("Unexpected request restaurant api error", exception.Message);
            Assert.Null(exception.ErrorDetail);
            Assert.NotNull(exception.InnerException);
        }
    }
}
