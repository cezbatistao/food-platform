package usecase

import (
    "context"
    "errors"
    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/gateway/mock"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
    "github.com/google/uuid"
    "github.com/stretchr/testify/assert"
    "testing"
    "time"

    "github.com/golang/mock/gomock"
)

func TestGetOrderByUuidErrorErrorWhenOrderGatewayRiseErr(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock := mock.NewMockOrderGateway(controller)

    getOrderByUuid := NewGetOrderByUuid(orderGatewayMock)

    ctx := context.Background()

    var errOrderGateway = errors.New("unexpected error at order gateway")

    orderUuid := uuid.New()

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(nil, errOrderGateway)

    orderReturned, err := getOrderByUuid.Execute(ctx, &orderUuid)

    assert.Nil(t, orderReturned)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errOrderGateway.Error())
}

func TestGetOrderByUuidErrorWhenOrderNotFound(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock := mock.NewMockOrderGateway(controller)

    ctx := context.Background()

    getOrderByUuid := NewGetOrderByUuid(orderGatewayMock)

    var errOrderGateway error

    orderUuid := uuid.New()

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(nil, errOrderGateway)

    orderReturned, err := getOrderByUuid.Execute(ctx, &orderUuid)

    orderNotFoundError := exceptions.OrderNotFoundError{OrderUuid: orderUuid}

    assert.Nil(t, orderReturned)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), orderNotFoundError.Error())
}

func TestGetOrderByUuidSuccess(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock := mock.NewMockOrderGateway(controller)

    getOrderByUuid := NewGetOrderByUuid(orderGatewayMock)

    ctx := context.Background()

    var errOrderGateway error

    orderUuid := uuid.New()

    orderItems := []domain.OrderItem{
        {Id: 1, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Pepperoni", Amount: 1, UnitValue: 33.99},
        {Id: 2, Uuid: uuid.New(), MenuItemUuid: uuid.New(), Name: "Mussarela", Amount: 2, UnitValue: 29.99},
    }

    order := domain.Order{Id: 1, Uuid: orderUuid, UserUuid: uuid.New(), Number: 1023, Status: domain.CREATED,
        Restaurant: domain.Restaurant{Uuid: uuid.New(), Name: "Pizza Hut"}, Items: orderItems, Total: 93.97,
        Payment: domain.PaymentOrder{Id: 1, Uuid: uuid.New(), Status: domain.PAID, TransactionId: "1233211235"},
        DateCreated: time.Now(), DateUpdated: time.Now(),
    }

    orderGatewayMock.EXPECT().GetByUuid(gomock.Any(), &orderUuid).Return(&order, errOrderGateway)

    orderReturned, err := getOrderByUuid.Execute(ctx, &orderUuid)

    assert.NotNil(t, orderReturned)
    assert.Nil(t, err)
}
