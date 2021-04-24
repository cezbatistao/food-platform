using System.Net.Http;
using System.Text.Json;
using food_order.Gateway.Http.Exception;
using food_order.Gateway.Http.Json;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace food_order.Gateway.Http
{
    public class RestaurantClient
    {
        private readonly IHttpClientFactory _clientFactory;
        private readonly IConfiguration _configuration;
        private readonly ILogger<RestaurantClient> _logger;

        public RestaurantClient(IConfiguration configuration, IHttpClientFactory clientFactory, 
            ILogger<RestaurantClient> logger)
        {
            _clientFactory = clientFactory;
            _configuration = configuration;
            _logger = logger;
        }

        public DataRestaurantResponse GetByUuid(string uuid)
        {
            var restaurantAppUrl = _configuration.GetValue<string>("RestaurantApp:Url");
            
            var request = new HttpRequestMessage(HttpMethod.Get,
                $"{restaurantAppUrl}/api/v1/restaurants/{uuid}");
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