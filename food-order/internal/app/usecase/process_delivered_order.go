package usecase

import (
    "context"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
)

type ProcessDeliveredOrder struct {
    changeOrderStatus
}

func NewProcessDeliveredOrder(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *ProcessDeliveredOrder {
    return &ProcessDeliveredOrder{*newChangeOrderStatus(orderGateway, orderSendGateway, transaction)}
}

func (c *ProcessDeliveredOrder) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) error {
    return c.execute(ctx, userUuid, orderUuid, domain.SHIPPED, domain.DELIVERED)
}
