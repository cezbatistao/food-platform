package usecase

import (
    "context"
    "errors"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/labstack/gommon/log"
)

type UpdateOrderAndNotify struct {
    orderGateway     gateway.OrderGateway
    orderSendGateway gateway.OrderSendGateway
    transaction      transaction.Transaction
    orderStatusEvent orderStatusEvent
}

func NewUpdateOrderAndNotify(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *UpdateOrderAndNotify {

    orderStatusEventDefault    := newOrderStatusEventDefault()
    orderStatusEventDelivered  := newOrderStatusEventDelivered(orderSendGateway, orderStatusEventDefault)
    orderStatusEventShipped    := newOrderStatusEventShipped(orderSendGateway, orderStatusEventDelivered)
    orderStatusEventCancelled  := newOrderStatusEventCancelled(orderSendGateway, orderStatusEventShipped)
    orderStatusEventProcessing := newOrderStatusEventProcessing(orderSendGateway, orderStatusEventCancelled)

    return &UpdateOrderAndNotify{orderGateway: orderGateway, orderSendGateway: orderSendGateway,
        transaction: transaction, orderStatusEvent: orderStatusEventProcessing}
}

func (c *UpdateOrderAndNotify) Execute(ctx context.Context, order *domain.Order) error {
    log.Infof("order to update: %+v", order)

    err := c.transaction.WithTransaction(ctx, func (ctxTx context.Context) error {
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
