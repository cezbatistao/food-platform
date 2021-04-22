using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using AutoFixture;
using food_order;
using food_order.Domain;
using food_order.Domain.Restaurant;
using food_order.Entrypoint.Rest.Json;
using food_order.Gateway;
using food_order.UseCase;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.TestHost;
using Microsoft.Extensions.DependencyInjection.Extensions;
using Moq;
using Newtonsoft.Json;
using test.Fixture;
using Xunit;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace test.Entrypoint.Rest
{
    public class OrderControllerIntegrationTest
    {
        private IFixture _fixture;

        private readonly HttpClient _client;
        
        private Mock<RegisterOrder> _mockRegisterOrder;

        public OrderControllerIntegrationTest()
        {
            _fixture = new AutoFixture.Fixture().Customize(new IntegrationTestCustomization());
            
            _mockRegisterOrder = new Mock<RegisterOrder>(null, null);
            
            // Arrange
            var server = new TestServer(new WebHostBuilder()
                .UseEnvironment("Testing")
                // .UseContentRoot(projectDir) ?
                .ConfigureTestServices(services => {
                    services.RemoveAll<RegisterOrder>();//Remove previous registration(s) of this service
                    services.RemoveAll<IOrderGateway>();//Remove previous registration(s) of this service
                    services.RemoveAll<IRestaurantGateway>();//Remove previous registration(s) of this service
                    services.TryAddTransient<RegisterOrder>(sp => _mockRegisterOrder.Object);
                })
                .UseStartup<Startup>());
            _client = server.CreateClient();
        }

        [Fact]
        public async void ShouldReturnHttpStatus200OnRegisterAOrder()
        {
            // given
            var registeredOrder = _fixture.Create<Order>();
            
            _mockRegisterOrder.Setup(s => 
                s.Execute(It.IsAny<Ordered>())
            ).Returns(registeredOrder)
                .Callback<Ordered>(orderedVerify =>
                {
                    Assert.NotNull(orderedVerify);
                    Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", orderedVerify.RestaurantUuid);
                    Assert.Single(orderedVerify.Items);
                    Assert.Collection(orderedVerify.Items,
                        orderedItem =>
                        {
                            Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderedItem.Uuid);
                            Assert.Equal(2, orderedItem.Amount);
                            Assert.Equal(31.99m, orderedItem.UnitValue);
                        });
                    Assert.Equal(63.98m, orderedVerify.Total);
                });
                
            var orderRequest = _fixture.Create<OrderRequest>();
            
            var httpContent = new StringContent(JsonSerializer.Serialize(orderRequest), Encoding.UTF8, "application/json");
            
            // when
            var response = await _client.PostAsync("/api/v1/orders", httpContent);
            
            // then
            Assert.Equal(HttpStatusCode.OK, response.StatusCode);
            
            // and
            var jsonFromResponse = await response.Content.ReadAsStringAsync();
            
            var dataResponse = JsonConvert.DeserializeObject<DataResponse<OrderResponse>>(jsonFromResponse);
            
            Assert.NotNull(dataResponse);
            Assert.NotNull(dataResponse.Data);
            Assert.Equal("27f1fc9a-6bbf-4490-b3e4-42a77059909d", dataResponse.Data.Uuid);
            Assert.NotNull(dataResponse.Data.Restaurant);
            Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", dataResponse.Data.Restaurant.Uuid);
            Assert.Equal("Domino's Pizza", dataResponse.Data.Restaurant.Name);
            Assert.Single(dataResponse.Data.Items);
            Assert.Collection(dataResponse.Data.Items,
                orderItemResponse =>
                {
                    Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderItemResponse.Uuid);
                    Assert.Equal("Mussarela", orderItemResponse.Name);
                    Assert.Equal(2, orderItemResponse.Amount);
                    Assert.Equal(31.99m, orderItemResponse.UnitValue);
                });
            Assert.Equal(63.98m, dataResponse.Data.Total);
        }
    }
}