package usecase

import (
    "context"
    "errors"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
)

type UpdateOrderStatus struct {
    orderGateway      gateway.OrderGateway
    orderSendGateway  gateway.OrderSendGateway
    transaction       transaction.Transaction
}

func NewUpdateOrderStatus(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *UpdateOrderStatus {
    return &UpdateOrderStatus{orderGateway: orderGateway, orderSendGateway: orderSendGateway,
        transaction: transaction}
}

func (c *UpdateOrderStatus) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID, orderStatus domain.OrderStatus) error {
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
    err = c.transaction.WithTransaction(ctx, func (ctxTx context.Context) error {
        order, err = c.orderGateway.Update(ctxTx, order)
        if err != nil {
            return err
        }

        if order.Status == domain.DELIVERED {
            return nil
        }

        return c.orderSendGateway.SendTo(ctxTx, order)
    })

    if err != nil {
        return err
    }

    return nil
}
