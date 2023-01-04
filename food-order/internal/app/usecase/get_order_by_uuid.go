package usecase

import (
    "context"
    
    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"

    "github.com/google/uuid"
)

type GetOrderByUuid struct {
    orderGateway gateway.OrderGateway
}

func NewGetOrderByUuid(orderGateway gateway.OrderGateway) *GetOrderByUuid {
    return &GetOrderByUuid{orderGateway: orderGateway}
}

func (u *GetOrderByUuid) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) (*domain.Order, error) {
    order, err := u.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
    if err != nil {
        return nil, err
    }

    return order, nil
}
