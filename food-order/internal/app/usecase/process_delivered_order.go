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

type ProcessDeliveredOrder struct {
    updateOrderAndNotify *UpdateOrderAndNotify
    orderGateway         gateway.OrderGateway
}

func NewProcessDeliveredOrder(updateOrderAndNotify *UpdateOrderAndNotify,
        orderGateway gateway.OrderGateway) *ProcessDeliveredOrder {
    return &ProcessDeliveredOrder{updateOrderAndNotify: updateOrderAndNotify, orderGateway: orderGateway}
}

func (c *ProcessDeliveredOrder) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID) error {
    log.Info("marking order as delivered")

    order, err := c.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
    if err != nil {
        return err
    }
    log.Infof("order to change status: %+v", order)

    if order.Status != domain.SHIPPED {
        return errors.New(fmt.Sprintf("order %s has status different of SHIPPED", order.Uuid))
    }

    order.Status = domain.DELIVERED

    return c.updateOrderAndNotify.Execute(ctx, order)
}
