package usecase

import (
    "context"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
)

type MarkOrderAsShipped struct {
    changeOrderStatus
}

func NewMarkOrderAsShipped(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *MarkOrderAsShipped {
    return &MarkOrderAsShipped{*newChangeOrderStatus(orderGateway, orderSendGateway, transaction)}
}

func (c *MarkOrderAsShipped) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) error {
    return c.execute(ctx, userUuid, orderUuid, domain.ACCEPTED, domain.SHIPPED)
}
