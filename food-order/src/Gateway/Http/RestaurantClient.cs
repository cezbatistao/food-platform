using System.IO;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway.Http.Exception;
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

        public DataRestaurantResponse GetByUuid(string uuid)
        {
            var request = new HttpRequestMessage(HttpMethod.Get,
                $"http://localhost:8882/api/v1/restaurants/{uuid}");
            request.Headers.Add("Accept", "application/json; charset=utf-8");
            request.Headers.Add("User-Agent", "HttpClientFactory-Sample");
            
            var client = _clientFactory.CreateClient();

            HttpResponseMessage response = null;
            try
            {
                response = client.Send(request);
            }
            catch (HttpRequestException ex)
            {
                throw new RequestRestApiException(
                    "9998", 
                    "requestRestApiException", 
                    "Unexpected request restaurant api error", 
                    ex
                );
            }

            var jsonFromResponse = response.Content.ReadAsStringAsync().Result;
            return JsonSerializer.Deserialize<DataRestaurantResponse>(jsonFromResponse);
        }
    }
}