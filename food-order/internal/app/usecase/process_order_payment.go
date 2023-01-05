package usecase

import (
    "context"
    "errors"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/exceptions"

    "github.com/google/uuid"
)

type ProcessOrderPayment struct {
    updateOrderAndNotify *UpdateOrderAndNotify
    orderGateway         gateway.OrderGateway
}

func NewProcessOrderPayment(updateOrderAndNotify *UpdateOrderAndNotify,
        orderGateway gateway.OrderGateway) *ProcessOrderPayment {
    return &ProcessOrderPayment{updateOrderAndNotify: updateOrderAndNotify,
        orderGateway: orderGateway}
}

func (c *ProcessOrderPayment) Execute(ctx context.Context, userUuid *uuid.UUID, orderUuid *uuid.UUID,
        paymentOrder *domain.PaymentOrder) error {
    order, err := c.orderGateway.GetByUuid(ctx, userUuid, orderUuid)
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

    return c.updateOrderAndNotify.Execute(ctx, order)
}
