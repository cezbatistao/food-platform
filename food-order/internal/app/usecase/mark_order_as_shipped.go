package usecase

import (
    "context"
    "errors"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
)

type MarkOrderAsShipped struct {
    updateOrderAndNotify *UpdateOrderAndNotify
    orderGateway         gateway.OrderGateway
}

func NewMarkOrderAsShipped(updateOrderAndNotify *UpdateOrderAndNotify, orderGateway gateway.OrderGateway) *MarkOrderAsShipped {
    return &MarkOrderAsShipped{updateOrderAndNotify: updateOrderAndNotify, orderGateway: orderGateway}
}

func (c *MarkOrderAsShipped) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) error {
    log.Info("marking order to shipped")

    order, err := c.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
    if err != nil {
        return err
    }
    log.Infof("order to change status: %+v", order)

    if order.Status != domain.PROCESSING {
        errorMessage := fmt.Sprintf("order %s has status different of PROCESSING", order.Uuid)
        log.Error(errorMessage)
        return errors.New(errorMessage)
    }

    order.Status = domain.SHIPPED

    return c.updateOrderAndNotify.Execute(ctx, order)
}
