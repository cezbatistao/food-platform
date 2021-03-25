package usecase

import (
    "errors"
    "fmt"
    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
    "github.com/google/uuid"
)

type ProcessOrderPayment struct {
    orderGateway     gateway.OrderGateway
    orderSendGateway gateway.OrderSendGateway
}

func NewProcessOrderPayment(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway) *ProcessOrderPayment {
    return &ProcessOrderPayment{orderGateway: orderGateway, orderSendGateway: orderSendGateway}
}

func (c *ProcessOrderPayment) Execute(orderUuid *uuid.UUID, paymentOrder *domain.PaymentOrder) error {
    order, err := c.orderGateway.GetByUuid(orderUuid)
    if err != nil {
        return err
    }
    if order == nil {
        return &exceptions.OrderNotFoundError{OrderUuid: *orderUuid}
    }
    if order.Status != domain.CREATED {
        return errors.New(fmt.Sprintf("order %s has status different than CREATED", order.Uuid))
    }

    if paymentOrder.Status == domain.PAID {
        order.Status = domain.PROCESSING
    } else {
        paymentOrder.TransactionId = ""
        order.Status = domain.CANCELLED
    }

    paymentOrder.Uuid = uuid.New()
    order.Payment = *paymentOrder
    order, err = c.orderGateway.Update(order)
    if err != nil {
        return err
    }

    if order.Status == domain.PROCESSING {
        err = c.orderSendGateway.SendProcessing(order)
    } else {
        err = c.orderSendGateway.SendCancelled(order)
    }

    if err != nil {
        return err
    }

    return nil
}