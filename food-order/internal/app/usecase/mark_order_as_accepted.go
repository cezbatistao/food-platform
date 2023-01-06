package usecase

import (
    "context"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
)

type MarkOrderAsAccepted struct {
    changeOrderStatus
}

func NewMarkOrderAsAccepted(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *MarkOrderAsAccepted {
    return &MarkOrderAsAccepted{*newChangeOrderStatus(orderGateway, orderSendGateway, transaction)}
}

func (c *MarkOrderAsAccepted) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) error {
    return c.execute(ctx, userUuid, orderUuid, domain.PROCESSING, domain.ACCEPTED)
}
