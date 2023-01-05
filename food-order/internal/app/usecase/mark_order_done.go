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

type MarkOrderDone struct {
    updateOrderAndNotify *UpdateOrderAndNotify
    orderGateway         gateway.OrderGateway
}

func NewMarkOrderDone(updateOrderAndNotify *UpdateOrderAndNotify, orderGateway gateway.OrderGateway) *MarkOrderDone {
    return &MarkOrderDone{updateOrderAndNotify: updateOrderAndNotify, orderGateway: orderGateway}
}

func (c *MarkOrderDone) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID, orderStatus domain.OrderStatus) error {
    log.Infof("user uuid[%+v] and order uuid[%+v] to change status to: %+v", userUuid, orderUuid, orderStatus)

    if orderStatus != domain.SHIPPED && orderStatus != domain.DELIVERED {
        return errors.New(fmt.Sprintf("orderStatus %s is different of SHIPPED or DELIVERED", orderStatus))
    }

    order, err := c.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
    if err != nil {
        return err
    }
    log.Infof("order to change status: %+v", order)

    if order.Status != domain.PROCESSING && order.Status != domain.SHIPPED {
        return errors.New(fmt.Sprintf("order %s has status different than PROCESSING or SHIPPED", order.Uuid))
    }

    order.Status = orderStatus

    return c.updateOrderAndNotify.Execute(ctx, order)
}
