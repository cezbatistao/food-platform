package usecase

import (
    "context"
    "errors"
    "fmt"
    "strings"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
)

type changeOrderStatus struct {
    orderGateway     gateway.OrderGateway
    orderSendGateway gateway.OrderSendGateway
    transaction      transaction.Transaction
    orderStatusEvent orderStatusEvent
}

func newChangeOrderStatus(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
    transaction transaction.Transaction) *changeOrderStatus {

    orderStatusEventDefault    := newOrderStatusEventDefault()
    orderStatusEventDelivered  := newOrderStatusEventDelivered(orderSendGateway, orderStatusEventDefault)
    orderStatusEventShipped    := newOrderStatusEventShipped(orderSendGateway, orderStatusEventDelivered)
    orderStatusEventAccepted   := newOrderStatusEventAccepted(orderSendGateway, orderStatusEventShipped)
    orderStatusEventCancelled  := newOrderStatusEventCancelled(orderSendGateway, orderStatusEventAccepted)
    orderStatusEventProcessing := newOrderStatusEventProcessing(orderSendGateway, orderStatusEventCancelled)

    return &changeOrderStatus{orderGateway: orderGateway, orderSendGateway: orderSendGateway,
        transaction: transaction, orderStatusEvent: orderStatusEventProcessing}
}

func (c *changeOrderStatus) execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID,
        fromOrderStatus domain.OrderStatus, toOrderStatus domain.OrderStatus) error {
    log.Infof("marking order to %s", strings.ToLower(toOrderStatus.GetOrderStatus()))

    order, err := c.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
    if err != nil {
        return err
    }
    log.Infof("order to change status: %+v", order)

    if order.Status != fromOrderStatus {
        errorMessage := fmt.Sprintf("order %s has status different of %s", order.Uuid,
            fromOrderStatus.GetOrderStatus())
        log.Error(errorMessage)
        return errors.New(errorMessage)
    }

    order.Status = toOrderStatus

    log.Infof("order to update: %+v", order)

    err = c.transaction.WithTransaction(ctx, func (ctxTx context.Context) error {
        order, err := c.orderGateway.Update(ctxTx, order)
        if err != nil {
            return err
        }

        return c.orderStatusEvent.process(ctxTx, order)
    })

    return err
}


type orderStatusEvent interface {
    process(context.Context, *domain.Order) error
    setNext(orderStatusEvent)
}


type orderStatusEventProcessing struct {
    orderSendGateway gateway.OrderSendGateway
    next orderStatusEvent
}

func newOrderStatusEventProcessing(orderSendGateway gateway.OrderSendGateway, next orderStatusEvent) *orderStatusEventProcessing {
    return &orderStatusEventProcessing{orderSendGateway: orderSendGateway, next: next}
}

func (d *orderStatusEventProcessing) process(ctx context.Context, order *domain.Order) error {
    if order.Status == domain.PROCESSING {
        return d.orderSendGateway.SendProcessing(ctx, order)
    }

    return d.next.process(ctx, order)
}

func (d *orderStatusEventProcessing) setNext(next orderStatusEvent) {
    d.next = next
}


type orderStatusEventCancelled struct {
    orderSendGateway gateway.OrderSendGateway
    next orderStatusEvent
}

func newOrderStatusEventCancelled(orderSendGateway gateway.OrderSendGateway, next orderStatusEvent) *orderStatusEventCancelled {
    return &orderStatusEventCancelled{orderSendGateway: orderSendGateway, next: next}
}

func (d *orderStatusEventCancelled) process(ctx context.Context, order *domain.Order) error {
    if order.Status == domain.CANCELLED {
        return d.orderSendGateway.SendCancelled(ctx, order)
    }

    return d.next.process(ctx, order)
}

func (d *orderStatusEventCancelled) setNext(next orderStatusEvent) {
    d.next = next
}


type orderStatusEventAccepted struct {
    orderSendGateway gateway.OrderSendGateway
    next orderStatusEvent
}

func newOrderStatusEventAccepted(orderSendGateway gateway.OrderSendGateway, next orderStatusEvent) *orderStatusEventAccepted {
    return &orderStatusEventAccepted{orderSendGateway: orderSendGateway, next: next}
}

func (d *orderStatusEventAccepted) process(ctx context.Context, order *domain.Order) error {
    if order.Status == domain.ACCEPTED {
        return d.orderSendGateway.SendAccepted(ctx, order)
    }

    return d.next.process(ctx, order)
}

func (d *orderStatusEventAccepted) setNext(next orderStatusEvent) {
    d.next = next
}


type orderStatusEventShipped struct {
    orderSendGateway gateway.OrderSendGateway
    next orderStatusEvent
}

func newOrderStatusEventShipped(orderSendGateway gateway.OrderSendGateway, next orderStatusEvent) *orderStatusEventShipped {
    return &orderStatusEventShipped{orderSendGateway: orderSendGateway, next: next}
}

func (d *orderStatusEventShipped) process(ctx context.Context, order *domain.Order) error {
    if order.Status == domain.SHIPPED {
        return d.orderSendGateway.SendShipped(ctx, order)
    }

    return d.next.process(ctx, order)
}

func (d *orderStatusEventShipped) setNext(next orderStatusEvent) {
    d.next = next
}


type orderStatusEventDelivered struct {
    orderSendGateway gateway.OrderSendGateway
    next orderStatusEvent
}

func newOrderStatusEventDelivered(orderSendGateway gateway.OrderSendGateway, next orderStatusEvent) *orderStatusEventDelivered {
    return &orderStatusEventDelivered{orderSendGateway: orderSendGateway, next: next}
}

func (d *orderStatusEventDelivered) process(ctx context.Context, order *domain.Order) error {
    if order.Status == domain.DELIVERED {
        return nil
    }

    return d.next.process(ctx, order)
}

func (d *orderStatusEventDelivered) setNext(next orderStatusEvent) {
    d.next = next
}


type orderStatusEventDefault struct {
    next orderStatusEvent
}

func newOrderStatusEventDefault() *orderStatusEventDefault {
    return &orderStatusEventDefault{}
}

func (d *orderStatusEventDefault) process(ctx context.Context, order *domain.Order) error {
    return errors.New(fmt.Sprintf("order %s with status %s cannot be processed", order.Uuid, order.Status.GetOrderStatus()))
}

func (d *orderStatusEventDefault) setNext(next orderStatusEvent) {
    d.next = next
}
