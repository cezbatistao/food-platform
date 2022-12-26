package usecase

import (
    "context"
    "database/sql"
    "errors"
    "fmt"
    "testing"
    "time"

    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/gateway/mock"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"

    "github.com/golang/mock/gomock"
    "github.com/google/uuid"
    "github.com/stretchr/testify/assert"
)

func TestProcessOrderPaymentErrorWhenOrderGatewayRiseErr(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    transactionId := "1233211235"
    orderUuid := uuid.New()

    var errOrderGateway = errors.New("unexpected error at order gateway")

    orderGatewayMock.EXPECT().GetByUuid(ctx, &orderUuid).Return(nil, errOrderGateway)

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errOrderGateway.Error())
}

func TestProcessOrderPaymentErrorWhenOrderNotFound(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    transactionId := "1233211235"
    orderUuid := uuid.New()

    var errOrderGateway error

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(nil, errOrderGateway)

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    orderNotFoundError := exceptions.OrderNotFoundError{OrderUuid: orderUuid}

    assert.Error(t, err)
    assert.Contains(t, err.Error(), orderNotFoundError.Error())
}

func TestProcessOrderPaymentErrorWhenOrderStatusDiffCreated(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    orderUuid := uuid.New()
    transactionId := "1233211235"

    var errOrderGateway error
    var errStatusDiffCreated = errors.New(fmt.Sprintf("order %s has status different than CREATED", orderUuid.String()))

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.PROCESSING,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, errOrderGateway)

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errStatusDiffCreated.Error())
}

func TestProcessOrderPaymentPaidWhenErrorOrderGatewayUpdate(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    var errOrderGateway = errors.New("unexpected error at order gateway")

    transactionId := "1233211235"
    orderUuid := uuid.New()

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.CREATED,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, nil)

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(1).Return(nil, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errOrderGateway.Error())
}

func TestProcessOrderPaymentPaidWhenErrorOrderSendGatewayUpdate(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    var errOrderGateway error
    var errOrderSendGatewayMock = errors.New("unexpected error at order send gateway")

    transactionId := "1233211235"
    orderUuid := uuid.New()

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.CREATED,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, errOrderGateway)

    var orderToReturn = new(domain.Order)
    orderToReturn.Status = domain.PROCESSING
    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(1).Return(orderToReturn, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(1).Return(errOrderSendGatewayMock)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errOrderSendGatewayMock.Error())
}

func TestProcessOrderPaymentPaidSuccess(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    ctx := context.Background()

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    var errOrderGateway error

    transactionId := "1233211235"
    orderUuid := uuid.New()

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.CREATED,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    pizzaPepperoniItem := findOrderItemByName(orderItems, "Pepperoni")
    pizzaMozzarellaItem := findOrderItemByName(orderItems, "Mussarela")

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, errOrderGateway)

    var orderArgumentCaptor *domain.Order
    var orderToReturn = new(domain.Order)
    orderToReturn.Status = domain.PROCESSING

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(1).Do(func (ctx context.Context, tx *sql.Tx, orderParameter *domain.Order) {
        orderArgumentCaptor = orderParameter
    }).Return(orderToReturn, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(1)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(0)

    paymentOrder := domain.PaymentOrder{Status: domain.PAID, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Nil(t, err)

    assert.Equal(t, order.Id, orderArgumentCaptor.Id)
    assert.NotEmpty(t, order.Uuid, orderArgumentCaptor.Uuid)
    assert.Equal(t, order.Number, orderArgumentCaptor.Number)
    assert.Equal(t, domain.PROCESSING, orderArgumentCaptor.Status)
    assert.NotEmpty(t, order.UserUuid, orderArgumentCaptor.UserUuid)
    assert.Equal(t, order.Restaurant.Uuid, orderArgumentCaptor.Restaurant.Uuid)
    assert.Equal(t, order.Restaurant.Name, orderArgumentCaptor.Restaurant.Name)
    assert.NotEmpty(t, orderArgumentCaptor.Payment)
    assert.NotEmpty(t, orderArgumentCaptor.Payment.Uuid)
    assert.Equal(t, domain.PAID, orderArgumentCaptor.Payment.Status)
    assert.Equal(t, transactionId, orderArgumentCaptor.Payment.TransactionId)
    assert.Equal(t, 93.97,orderArgumentCaptor.Total)

    orderItemPepperoni := findOrderItemByName(orderArgumentCaptor.Items, "Pepperoni")
    assert.Equal(t, pizzaPepperoniItem.Id, orderItemPepperoni.Id)
    assert.Equal(t, pizzaPepperoniItem.Uuid, orderItemPepperoni.Uuid)
    assert.Equal(t, pizzaPepperoniItem.MenuItemUuid, orderItemPepperoni.MenuItemUuid)
    assert.Equal(t, pizzaPepperoniItem.Amount, orderItemPepperoni.Amount)
    assert.Equal(t, pizzaPepperoniItem.UnitValue, orderItemPepperoni.UnitValue)

    orderItemMussarela := findOrderItemByName(orderArgumentCaptor.Items, "Mussarela")
    assert.Equal(t, pizzaMozzarellaItem.Id, orderItemMussarela.Id)
    assert.Equal(t, pizzaMozzarellaItem.Uuid, orderItemMussarela.Uuid)
    assert.Equal(t, pizzaMozzarellaItem.MenuItemUuid, orderItemMussarela.MenuItemUuid)
    assert.Equal(t, pizzaMozzarellaItem.Amount, orderItemMussarela.Amount)
    assert.Equal(t, pizzaMozzarellaItem.UnitValue, orderItemMussarela.UnitValue)
}

func TestProcessOrderPaymentRefusedSuccess(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    transactionFake      := mock.NewTransactionMock()

    ctx := context.Background()

    processOrderPayment := NewProcessOrderPayment(orderGatewayMock, orderSendGatewayMock, transactionFake)

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    pizzaPepperoniItem := findOrderItemByName(orderItems, "Pepperoni")
    pizzaMozzarellaItem := findOrderItemByName(orderItems, "Mussarela")

    var errOrderGateway error

    transactionId := "1233211235"
    orderUuid := uuid.New()

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.CREATED,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, errOrderGateway)

    var orderArgumentCaptor *domain.Order
    var orderToReturn = new(domain.Order)
    orderToReturn.Status = domain.CANCELLED

    orderGatewayMock.EXPECT().UpdateWithTx(gomock.Any(), gomock.Any(), gomock.Any()).Times(1).Do(func (ctx context.Context, tx *sql.Tx, orderParameter *domain.Order) {
        orderArgumentCaptor = orderParameter
    }).Return(orderToReturn, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendProcessing(gomock.Any(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCancelled(gomock.Any(), gomock.Any()).Times(1)

    paymentOrder := domain.PaymentOrder{Status: domain.REFUSED, TransactionId: transactionId}

    err := processOrderPayment.Execute(ctx, &orderUuid, &paymentOrder)

    assert.Nil(t, err)

    assert.Equal(t, order.Id, orderArgumentCaptor.Id)
    assert.NotEmpty(t, order.Uuid, orderArgumentCaptor.Uuid)
    assert.Equal(t, order.Number, orderArgumentCaptor.Number)
    assert.Equal(t, domain.CANCELLED, orderArgumentCaptor.Status)
    assert.NotEmpty(t, order.UserUuid, orderArgumentCaptor.UserUuid)
    assert.Equal(t, order.Restaurant.Uuid, orderArgumentCaptor.Restaurant.Uuid)
    assert.Equal(t, order.Restaurant.Name, orderArgumentCaptor.Restaurant.Name)
    assert.NotEmpty(t, orderArgumentCaptor.Payment)
    assert.NotEmpty(t, orderArgumentCaptor.Payment.Uuid)
    assert.Equal(t, domain.REFUSED, orderArgumentCaptor.Payment.Status)
    assert.Empty(t, orderArgumentCaptor.Payment.TransactionId)
    assert.Equal(t, 93.97,orderArgumentCaptor.Total)

    orderItemPepperoni := findOrderItemByName(orderArgumentCaptor.Items, "Pepperoni")
    assert.Equal(t, pizzaPepperoniItem.Id, orderItemPepperoni.Id)
    assert.Equal(t, pizzaPepperoniItem.Uuid, orderItemPepperoni.Uuid)
    assert.Equal(t, pizzaPepperoniItem.MenuItemUuid, orderItemPepperoni.MenuItemUuid)
    assert.Equal(t, pizzaPepperoniItem.Amount, orderItemPepperoni.Amount)
    assert.Equal(t, pizzaPepperoniItem.UnitValue, orderItemPepperoni.UnitValue)

    orderItemMussarela := findOrderItemByName(orderArgumentCaptor.Items, "Mussarela")
    assert.Equal(t, pizzaMozzarellaItem.Id, orderItemMussarela.Id)
    assert.Equal(t, pizzaMozzarellaItem.Uuid, orderItemMussarela.Uuid)
    assert.Equal(t, pizzaMozzarellaItem.MenuItemUuid, orderItemMussarela.MenuItemUuid)
    assert.Equal(t, pizzaMozzarellaItem.Amount, orderItemMussarela.Amount)
    assert.Equal(t, pizzaMozzarellaItem.UnitValue, orderItemMussarela.UnitValue)
}
