using System.Collections.Generic;
using AutoMapper;
using food_order.Domain;
using food_order.Domain.Exception;
using food_order.Domain.Restaurant;
using food_order.Gateway.Database.Model;
using food_order.Gateway.Http.Exception;
using food_order.Gateway.Http.Json;

namespace food_order.Gateway.Http
{
    public class RestaurantGatewayImpl: IRestaurantGateway
    {
        private readonly RestaurantClient _restaurantClient;
        private readonly IMapper _mapper;

        public RestaurantGatewayImpl(RestaurantClient restaurantClient, IMapper mapper)
        {
            _restaurantClient = restaurantClient;
            _mapper = mapper;
        }

        public RestaurantDetail findById(string uuid)
        {
            var dataRestaurantResponse = _restaurantClient.GetByUuid(uuid);

            if (dataRestaurantResponse.Error is null)
            {
                var restaurantDetail = _mapper.Map<RestaurantDetail>(dataRestaurantResponse.Restaurant);
                return restaurantDetail;
            }

            if (dataRestaurantResponse.Error.Code.Equals("0003"))
            {
                throw new EntityNotFoundException(
                    "0001", 
                    "entityNotFoundException", 
                    $"Restaurant {uuid} don't exists"
                );                    
            }
            
            throw new RequestRestApiException(
                "9998", 
                "requestRestApiException", 
                "Unexpected request restaurant api error", 
                dataRestaurantResponse.Error
            );
        }
    }
    
    public class OrderProfile : Profile
    {
        public OrderProfile()
        {
            CreateMap<RestaurantResponse, RestaurantDetail>();
            CreateMap<MenuItemResponse, MenuItem>();
        }
    }
}
