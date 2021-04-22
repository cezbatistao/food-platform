using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
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
using Xunit;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace test.Entrypoint.Rest
{
    public class OrderControllerIntegrationTest
    {
        private readonly TestServer _server;
        private readonly HttpClient _client;
        
        private Mock<RegisterOrder> _mockRegisterOrder;

        public OrderControllerIntegrationTest()
        {
            _mockRegisterOrder = new Mock<RegisterOrder>(null, null);
            
            // Arrange
            _server = new TestServer(new WebHostBuilder()
                .UseEnvironment("Testing")
                // .UseContentRoot(projectDir) ?
                .ConfigureTestServices(services => {
                    services.RemoveAll<RegisterOrder>();//Remove previous registration(s) of this service
                    services.RemoveAll<IOrderGateway>();//Remove previous registration(s) of this service
                    services.RemoveAll<IRestaurantGateway>();//Remove previous registration(s) of this service
                    services.TryAddTransient<RegisterOrder>(sp => _mockRegisterOrder.Object);
                })
                .UseStartup<Startup>());
            _client = _server.CreateClient();
        }

        [Fact]
        public async void ShouldReturnHttpStatus200OnRegisterAOrder()
        {
            // given
            var registeredOrder = new Order(1, 
                "9e814a9a-4465-4312-adee-a9d3af86d895", 
                new Restaurant("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", "Domino's Pizza"), 
                new List<OrderItem>
                {
                    new (1, "843bfe62-9543-11eb-a8b3-0242ac130003", "Pepperoni", 1, 33.99m),
                    new (2, "88e3812e-9543-11eb-a8b3-0242ac130003", "Mussarela", 2, 31.99m)
                }, 
                97.97m);
            
            _mockRegisterOrder.Setup(s => 
                s.Execute(It.IsAny<Ordered>())
            ).Returns(registeredOrder);
                // .Callback<Ordered>(orderedVerify =>
                // {
                //     Assert.True(false);
                // });
            
            var itemRequest = new ItemRequest("88e3812e-9543-11eb-a8b3-0242ac130003", 2, 31.99m);
            var orderRequest = new OrderRequest("303f58dd-b5a4-496b-ae03-5510c1854acf", 
                new List<ItemRequest> { itemRequest });
            
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
            Assert.Equal("9e814a9a-4465-4312-adee-a9d3af86d895", dataResponse.Data.Uuid);
            Assert.NotNull(dataResponse.Data.Restaurant);
            Assert.Equal("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", dataResponse.Data.Restaurant.Uuid);
            Assert.Equal("Domino's Pizza", dataResponse.Data.Restaurant.Name);
            Assert.Equal(2, dataResponse.Data.Items.Count);
            Assert.Collection(dataResponse.Data.Items,
                orderItemResponse =>
                {
                    Assert.Equal("843bfe62-9543-11eb-a8b3-0242ac130003", orderItemResponse.Uuid);
                    Assert.Equal("Pepperoni", orderItemResponse.Name);
                    Assert.Equal(33.99m, orderItemResponse.UnitValue);
                },
                orderItemResponse =>
                {
                    Assert.Equal("88e3812e-9543-11eb-a8b3-0242ac130003", orderItemResponse.Uuid);
                    Assert.Equal("Mussarela", orderItemResponse.Name);
                    Assert.Equal(31.99m, orderItemResponse.UnitValue);
                });
            Assert.Equal(97.97m, dataResponse.Data.Total);
        }
    }
}