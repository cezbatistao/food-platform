package usecase

import (
    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
    "github.com/google/uuid"
)

type GetOrderByUuid struct {
    orderGateway gateway.OrderGateway
}

func NewGetOrderByUuid(orderGateway gateway.OrderGateway) *GetOrderByUuid {
    return &GetOrderByUuid{orderGateway: orderGateway}
}

func (u *GetOrderByUuid) Execute(uuid *uuid.UUID) (*domain.Order, error) {
    order, err := u.orderGateway.GetByUuid(uuid)
    if err != nil {
        return nil, err
    }
    if order == nil {
        return nil, &exceptions.OrderNotFoundError{OrderUuid: *uuid}
    }

    return order, nil
}
