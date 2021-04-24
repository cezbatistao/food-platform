using System.IO;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway.Http.Json;

namespace food_order.Gateway.Http
{
    public class RestaurantClient
    {
        private readonly IHttpClientFactory _clientFactory;

        public RestaurantClient(IHttpClientFactory clientFactory)
        {
            _clientFactory = clientFactory;
        }

        public Restaurant GetByUuid(string uuid)
        {
            var request = new HttpRequestMessage(HttpMethod.Get,
                $"http://localhost:8882/api/v1/restaurants/{uuid}");
            request.Headers.Add("Accept", "application/json; charset=utf-8");
            request.Headers.Add("User-Agent", "HttpClientFactory-Sample");
            
            var client = _clientFactory.CreateClient();
            
            var response = client.Send(request);
            
            if (response.IsSuccessStatusCode)
            {
                var stream = response.Content.ReadAsStream();
                var jsonFromResponse = new StreamReader(stream).ReadToEnd();
                var dataResponse = JsonSerializer.Deserialize<DataResponse<RestaurantResponse>>(jsonFromResponse);

                // TODO if dataResponse is null?
                
                return new Restaurant(dataResponse.Data.Uuid, dataResponse.Data.Name);
            }

            throw new EntityNotFoundException("0000", "0000", "0000");
        }
    }
}