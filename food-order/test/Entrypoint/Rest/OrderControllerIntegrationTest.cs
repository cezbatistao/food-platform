using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using AutoFixture;
using food_order;
using food_order.Domain;
using food_order.Domain.Exception;
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
                .ConfigureTestServices(services => {
                    services.RemoveAll<RegisterOrder>();//Remove previous registration(s) of this service
                    services.RemoveAll<IOrderGateway>();//Remove previous registration(s) of this service
                    services.RemoveAll<IRestaurantGateway>();//Remove previous registration(s) of this service
                    services.TryAddTransient<RegisterOrder>(sp => _mockRegisterOrder.Object);
                })
                .UseStartup<Startup>());
            _client = server.CreateClient();
        }
        
        public static IEnumerable<object[]> Data =>
            new List<object[]>
                {
                    new object[] { null, null },
                    new object[] { "", new List<ItemRequest>() },
                    new object[] { "  ", null },
            };
        
        [Theory]
        [MemberData(nameof(Data))]
        public async void ShouldValidationInputBody(string restaurantUuid, List<ItemRequest> itemsRequest)
        {
            // given
            var orderRequest = new OrderRequest(restaurantUuid, itemsRequest);
            
            var httpContent = new StringContent(JsonSerializer.Serialize(orderRequest), Encoding.UTF8, "application/json");
            
            // when
            var response = await _client.PostAsync("/api/v1/orders", httpContent);
            
            // then
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
            
            // and
            var jsonFromResponse = response.Content.ReadAsStringAsync().Result;
            
            var errorResponse = JsonConvert.DeserializeObject<ErrorResponse>(jsonFromResponse);
            
            Assert.NotNull(errorResponse);
            Assert.NotNull(errorResponse.Error);
            Assert.Equal("0003", errorResponse.Error.Code);
            Assert.Equal("Verify list of field with errors", errorResponse.Error.Message);
            Assert.Equal(2, errorResponse.Error.FieldErrors.Count);
            Assert.Collection(errorResponse.Error.FieldErrors,
                fieldError =>
                {
                    Assert.Equal("Items", fieldError.Field);
                    Assert.Equal(new List<string>() { "'Items' must not be empty." }, fieldError.Errors);
                }, 
                fieldError =>{
                    Assert.Equal("RestaurantUuid", fieldError.Field);
                    Assert.Equal(new List<string>() { "'Restaurant Uuid' must not be empty." }, fieldError.Errors);
                });
        }
        
        [Theory]
        [InlineData(null, 0, 0.0)]
        [InlineData("", -1, -0.1)]
        [InlineData("    ", -23, -3243.98)]
        public async void ShouldValidationInputBodyNestedItems(string itemUuid, int amount, decimal unitValue)
        {
            // given
            var orderRequest = new OrderRequest("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c", 
                new List<ItemRequest>
                {
                    new ItemRequest(itemUuid, amount, unitValue)
                });
            
            var httpContent = new StringContent(JsonSerializer.Serialize(orderRequest), Encoding.UTF8, "application/json");
            
            // when
            var response = await _client.PostAsync("/api/v1/orders", httpContent);
            
            // then
            Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
            
            // and
            var jsonFromResponse = response.Content.ReadAsStringAsync().Result;
            
            var errorResponse = JsonConvert.DeserializeObject<ErrorResponse>(jsonFromResponse);
            
            Assert.NotNull(errorResponse);
            Assert.NotNull(errorResponse.Error);
            Assert.Equal("0003", errorResponse.Error.Code);
            Assert.Equal("Verify list of field with errors", errorResponse.Error.Message);
            Assert.Equal(3, errorResponse.Error.FieldErrors.Count);
            Assert.Collection(errorResponse.Error.FieldErrors,
                fieldError =>
                {
                    Assert.Equal("Items[0].Uuid", fieldError.Field);
                    Assert.Equal(new List<string>() { "'Uuid' must not be empty." }, fieldError.Errors);
                }, 
                fieldError => {
                    Assert.Equal("Items[0].Amount", fieldError.Field);
                    Assert.Equal(new List<string>() { "'Amount' must greater than zero." }, fieldError.Errors);
                }, 
                fieldError => {
                    Assert.Equal("Items[0].UnitValue", fieldError.Field);
                    Assert.Equal(new List<string>() { "'Unit Value' must greater than zero." }, fieldError.Errors);
                }
            );
        }
        
        [Fact]
        public async void ShouldReturnHttpStatus404()
        {
            // given
            var orderRequest = _fixture.Create<OrderRequest>();
            
            _mockRegisterOrder.Setup(s => 
                s.Execute(It.IsAny<Ordered>())
            ).Throws(new EntityNotFoundException(
                "0001", "entityNotFoundException", $"Restaurant {orderRequest.RestaurantUuid} don't exists"));

            var httpContent = new StringContent(JsonSerializer.Serialize(orderRequest), Encoding.UTF8, "application/json");
            
            // when
            var response = await _client.PostAsync("/api/v1/orders", httpContent);
            
            // then
            Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
            
            // and
            var jsonFromResponse = await response.Content.ReadAsStringAsync();
            
            var errorResponse = JsonConvert.DeserializeObject<ErrorResponse>(jsonFromResponse);
            
            Assert.NotNull(errorResponse);
            Assert.NotNull(errorResponse.Error);
            Assert.Equal("0001", errorResponse.Error.Code);
            Assert.Equal($"Restaurant {orderRequest.RestaurantUuid} don't exists", errorResponse.Error.Message);
            Assert.Null(errorResponse.Error.FieldErrors);
        }
        
        [Fact]
        public async void ShouldReturnHttpStatus422()
        {
            // given
            var orderRequest = _fixture.Create<OrderRequest>();
            
            _mockRegisterOrder.Setup(s => 
                s.Execute(It.IsAny<Ordered>())
            ).Throws(new InvalidOrderException(
                "0002", "invalidOrderException", "Order with invalid items"));

            var httpContent = new StringContent(JsonSerializer.Serialize(orderRequest), Encoding.UTF8, "application/json");
            
            // when
            var response = await _client.PostAsync("/api/v1/orders", httpContent);
            
            // then
            Assert.Equal(HttpStatusCode.UnprocessableEntity, response.StatusCode);
            
            // and
            var jsonFromResponse = await response.Content.ReadAsStringAsync();
            
            var errorResponse = JsonConvert.DeserializeObject<ErrorResponse>(jsonFromResponse);
            
            Assert.NotNull(errorResponse);
            Assert.NotNull(errorResponse.Error);
            Assert.Equal("0002", errorResponse.Error.Code);
            Assert.Equal("Order with invalid items", errorResponse.Error.Message);
            Assert.Null(errorResponse.Error.FieldErrors);
        }

        [Fact]
        public async void ShouldReturnHttpStatus20OAndRegisterAOrder()
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