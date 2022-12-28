package usecase

import (
    "context"
    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
)

type GetOrdersFromUser struct {
    orderGateway gateway.OrderGateway
}

func NewGetOrdersFromUser(orderGateway gateway.OrderGateway) *GetOrdersFromUser {
    return &GetOrdersFromUser{orderGateway: orderGateway}
}

func (u *GetOrdersFromUser) Execute(ctx context.Context, orderPageable *domain.OrderPageable) (*domain.Page, error) {
    orderPage, err := u.orderGateway.ListAllByUserUuid(ctx, orderPageable)
    if err != nil {
        return nil, err
    }

    return orderPage, nil
}
