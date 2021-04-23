using System;
using System.Collections.Generic;
using System.Linq;
using AutoMapper;
using food_order.Domain;
using food_order.Domain.Restaurant;
using food_order.Gateway.Database.Data;
using food_order.Gateway.Database.Model;

namespace food_order.Gateway.Database
{
    public class OrderGatewayImpl: IOrderGateway
    {
        private readonly OrderContext _context;
        private readonly IMapper _mapper;

        public OrderGatewayImpl(OrderContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public Order register(Order order)
        {
            var orderModel = _mapper.Map<OrderModel>(order);
            var orderModelSaved = _context.OrderModel.Add(orderModel);
            _context.SaveChanges();
            return _mapper.Map(orderModelSaved.Entity, order);
        }
    }

    class OrderProfile : Profile
    {
        public OrderProfile()
        {
            CreateMap<Order, OrderModel>()
                .ReverseMap()
                .ForMember(d=>d.Restaurant, s=> s.Ignore());
            CreateMap<OrderItem, OrderItemModel>()
                .ReverseMap();
        }
    }
}
