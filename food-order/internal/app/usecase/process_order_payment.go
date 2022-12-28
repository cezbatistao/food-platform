package usecase

import (
    "context"
    "errors"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/exceptions"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/google/uuid"
)

type ProcessOrderPayment struct {
    orderGateway     gateway.OrderGateway
    orderSendGateway gateway.OrderSendGateway
    transaction      transaction.Transaction
}

func NewProcessOrderPayment(orderGateway gateway.OrderGateway,
        orderSendGateway gateway.OrderSendGateway,
        transaction transaction.Transaction) *ProcessOrderPayment {
    return &ProcessOrderPayment{orderGateway: orderGateway,
        orderSendGateway: orderSendGateway, transaction: transaction}
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
    err = c.transaction.WithTransaction(ctx, func(ctxTx context.Context) error {
        order, err = c.orderGateway.Update(ctxTx, order)
        if err != nil {
            return err
        }

        if order.Status == domain.PROCESSING {
            err = c.orderSendGateway.SendProcessing(ctx, order)
        } else {
            err = c.orderSendGateway.SendCancelled(ctx, order)
        }

        if err != nil {
            return err
        }

        return err
    })

    return err
}
