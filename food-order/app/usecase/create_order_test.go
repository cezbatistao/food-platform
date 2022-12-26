package usecase

import (
    "context"
    "database/sql"
    "errors"

    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/gateway/mock"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
    transactionMock "github.com/cezbatistao/food-platform/food-order/pkg/transaction/mock"
    "github.com/google/uuid"
    "testing"

    "github.com/golang/mock/gomock"
    "github.com/stretchr/testify/assert"
)

func TestCreateOrderErrorWhenRestaurantGatewayRiseErr(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    restaurantGateayMock := mock.NewMockRestaurantGateway(controller)
    transaction          := transactionMock.NewTransactionMock()

    createOrder := NewCreateOrder(orderGatewayMock, orderSendGatewayMock, restaurantGateayMock, transaction)

    ctx := context.Background()

    restaurantDetail, menuItems := buildRestaurantDetail()
    pizzaPepperoniItem := findMenuItemByName(menuItems, "Pepperoni")
    pizzaMozzarellaItem := findMenuItemByName(menuItems, "Mussarela")

    var menuItemsUuid = new([]uuid.UUID)
    var errRestaurantGateway = errors.New("unexpected error at restaurant gateway")

    restaurantUuid := restaurantDetail.Uuid
    userUuid       := uuid.New()
    *menuItemsUuid = []uuid.UUID{ pizzaPepperoniItem.Uuid, pizzaMozzarellaItem.Uuid }

    restaurantGateayMock.EXPECT().GetDetailByUuid(
        restaurantUuid, menuItemsUuid).Return(
            nil, errRestaurantGateway)
    orderGatewayMock.EXPECT().SaveWithTx(gomock.Any(), gomock.Nil(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCreated(gomock.Any(), gomock.Any()).Times(0)

    solicitationOrder := buildSolicitationOfOrder(restaurantUuid, userUuid,
        map[domain.MenuItem]int{ *pizzaPepperoniItem: 1, *pizzaMozzarellaItem: 2 })

    _, err := createOrder.Execute(ctx, solicitationOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errRestaurantGateway.Error())
}

func TestCreateOrderErrorWhenRestaurantNotFound(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    restaurantGateayMock := mock.NewMockRestaurantGateway(controller)
    transaction          := transactionMock.NewTransactionMock()

    createOrder := NewCreateOrder(orderGatewayMock, orderSendGatewayMock, restaurantGateayMock, transaction)

    ctx := context.Background()

    restaurantDetail, menuItems := buildRestaurantDetail()
    pizzaPepperoniItem := findMenuItemByName(menuItems, "Pepperoni")
    pizzaMozzarellaItem := findMenuItemByName(menuItems, "Mussarela")

    var menuItemsUuid = new([]uuid.UUID)
    var errRestaurantGateway error

    restaurantUuid := restaurantDetail.Uuid
    userUuid       := uuid.New()
    *menuItemsUuid = []uuid.UUID{ pizzaPepperoniItem.Uuid, pizzaMozzarellaItem.Uuid }

    restaurantGateayMock.EXPECT().GetDetailByUuid(
        restaurantUuid, menuItemsUuid).Return(
            nil, errRestaurantGateway)

    orderGatewayMock.EXPECT().SaveWithTx(gomock.Any(), gomock.Nil(), gomock.Any()).Times(0)
    orderSendGatewayMock.EXPECT().SendCreated(gomock.Any(), gomock.Any()).Times(0)

    solicitationOrder := buildSolicitationOfOrder(restaurantUuid, userUuid,
        map[domain.MenuItem]int{ *pizzaPepperoniItem: 1, *pizzaMozzarellaItem: 2 })

    _, err := createOrder.Execute(ctx, solicitationOrder)

    restaurantNotFoundErrorExpected := exceptions.RestaurantNotFoundError{RestaurantUuid: restaurantUuid}

    assert.Error(t, err)
    assert.Contains(t, err.Error(), restaurantNotFoundErrorExpected.Error())
}

func TestCreateOrderErrorWhenSaveOrder(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    restaurantGateayMock := mock.NewMockRestaurantGateway(controller)
    transaction          := transactionMock.NewTransactionMock()

    createOrder := NewCreateOrder(orderGatewayMock, orderSendGatewayMock, restaurantGateayMock, transaction)

    ctx := context.Background()

    restaurantDetail, menuItems := buildRestaurantDetail()
    pizzaPepperoniItem  := findMenuItemByName(menuItems, "Pepperoni")
    pizzaMozzarellaItem := findMenuItemByName(menuItems, "Mussarela")

    var errRestaurantGateway error
    var errOrderGateway = errors.New("unexpected error at order gateway")

    restaurantUuid := restaurantDetail.Uuid
    userUuid       := uuid.New()
    menuItemsUuid  := []uuid.UUID{ pizzaPepperoniItem.Uuid, pizzaMozzarellaItem.Uuid }

    restaurantGateayMock.EXPECT().GetDetailByUuid(
        restaurantUuid, &menuItemsUuid).Return(
            &restaurantDetail, errRestaurantGateway)

    var orderToReturn = new(domain.Order)
    orderGatewayMock.EXPECT().SaveWithTx(gomock.Any(), gomock.Nil(), gomock.Any()).Times(1).Return(orderToReturn, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendCreated(gomock.Any(), gomock.Any()).Times(0)

    solicitationOrder := buildSolicitationOfOrder(restaurantUuid, userUuid,
        map[domain.MenuItem]int{ *pizzaPepperoniItem: 1, *pizzaMozzarellaItem: 2 })

    _, err := createOrder.Execute(ctx, solicitationOrder)

    assert.Error(t, err)
    assert.Contains(t, err.Error(), errOrderGateway.Error())
}

func TestCreateOrderSuccess(t *testing.T) {
    controller := gomock.NewController(t)
    defer controller.Finish()

    orderGatewayMock     := mock.NewMockOrderGateway(controller)
    orderSendGatewayMock := mock.NewMockOrderSendGateway(controller)
    restaurantGateayMock := mock.NewMockRestaurantGateway(controller)
    transaction          := transactionMock.NewTransactionMock()

    createOrder := NewCreateOrder(orderGatewayMock, orderSendGatewayMock, restaurantGateayMock, transaction)

    ctx := context.Background()

    restaurantDetail, menuItems := buildRestaurantDetail()
    pizzaPepperoniItem := findMenuItemByName(menuItems, "Pepperoni")
    pizzaMozzarellaItem := findMenuItemByName(menuItems, "Mussarela")

    var errRestaurantGateway error
    var errOrderGateway error

    restaurantUuid := restaurantDetail.Uuid
    userUuid       := uuid.New()
    menuItemsUuid  := []uuid.UUID{ pizzaPepperoniItem.Uuid, pizzaMozzarellaItem.Uuid }

    restaurantGateayMock.EXPECT().GetDetailByUuid(
        restaurantUuid, &menuItemsUuid).Return(
            &restaurantDetail, errRestaurantGateway)

    var orderArgumentCaptor *domain.Order
    var orderToReturn = new(domain.Order)

    orderGatewayMock.EXPECT().SaveWithTx(gomock.Any(), gomock.Nil(), gomock.Any()).Do(func (ctx context.Context, tx *sql.Tx, orderParameter *domain.Order) {
        orderArgumentCaptor = orderParameter
    }).Return(orderToReturn, errOrderGateway)

    orderSendGatewayMock.EXPECT().SendCreated(gomock.Any(), gomock.Any()).Times(1)

    solicitationOrder := buildSolicitationOfOrder(restaurantUuid, userUuid,
        map[domain.MenuItem]int{ *pizzaPepperoniItem: 1, *pizzaMozzarellaItem: 2 })

    _, err := createOrder.Execute(ctx, solicitationOrder)

    assert.Nil(t, err)

    assert.Equal(t, 0, orderArgumentCaptor.Id)
    assert.NotEmpty(t, orderArgumentCaptor.Uuid)
    assert.Equal(t, 0, orderArgumentCaptor.Number)
    assert.Equal(t, domain.CREATED, orderArgumentCaptor.Status)
    assert.NotEmpty(t, userUuid, orderArgumentCaptor.UserUuid)
    assert.Equal(t, restaurantUuid, orderArgumentCaptor.Restaurant.Uuid)
    assert.Equal(t, restaurantDetail.Name, orderArgumentCaptor.Restaurant.Name)
    assert.Empty(t, orderArgumentCaptor.Payment)
    assert.Equal(t, 93.97, orderArgumentCaptor.Total)
    assert.Len(t, orderArgumentCaptor.Items, 2)

    orderItemPepperoni := findOrderItemByName(orderArgumentCaptor.Items, "Pepperoni")
    assert.Equal(t, 0, orderItemPepperoni.Id)
    assert.NotEmpty(t, orderItemPepperoni.Uuid)
    assert.Equal(t, pizzaPepperoniItem.Uuid, orderItemPepperoni.MenuItemUuid)
    assert.Equal(t, 1, orderItemPepperoni.Amount)
    assert.Equal(t, pizzaPepperoniItem.UnitValue, orderItemPepperoni.UnitValue)

    orderItemMussarela := findOrderItemByName(orderArgumentCaptor.Items, "Mussarela")
    assert.Equal(t, 0, orderItemMussarela.Id)
    assert.NotEmpty(t, orderItemMussarela.Uuid)
    assert.Equal(t, pizzaMozzarellaItem.Uuid, orderItemMussarela.MenuItemUuid)
    assert.Equal(t, 2, orderItemMussarela.Amount)
    assert.Equal(t, pizzaMozzarellaItem.UnitValue, orderItemMussarela.UnitValue)

}

func buildRestaurantDetail() (domain.RestaurantDetail, []domain.MenuItem) {
    menuItems := make([]domain.MenuItem, 0)

    pizzaPepperoniItem := domain.MenuItem{Uuid: uuid.New(), Name: "Pepperoni", UnitValue: 33.99}
    menuItems = append(menuItems, pizzaPepperoniItem)

    pizzaMozzarellaItem := domain.MenuItem{Uuid: uuid.New(), Name: "Mussarela", UnitValue: 29.99}
    menuItems = append(menuItems, pizzaMozzarellaItem)

    restaurantDetail := domain.RestaurantDetail{Uuid: uuid.New(), Name: "Pizza Hut", Items: menuItems}
    return restaurantDetail, menuItems
}

func buildSolicitationOfOrder(restaurantUuid uuid.UUID, userUuid uuid.UUID, items map[domain.MenuItem]int) *domain.SolicitationOfOrder {
    solicitationOrderItems := make([]domain.SolicitationOrderItem, 0)
    for key, value := range items {
        solicitationOrderItems = append(solicitationOrderItems, domain.SolicitationOrderItem{
            MenuItemUuid: key.Uuid, Amount: value, UnitValue: key.UnitValue})
    }

    solicitationOrder := &domain.SolicitationOfOrder{RestaurantUuid: restaurantUuid, UserUuid: userUuid,
        Items: solicitationOrderItems}

    return solicitationOrder
}

func findMenuItemByName(menuItems []domain.MenuItem, name string) *domain.MenuItem {
    for _, menuItem := range menuItems {
        if menuItem.Name == name {
            return &menuItem
        }
    }

    return nil
}

func findOrderItemByName(orderItems []domain.OrderItem, name string) *domain.OrderItem {
    for _, orderItem := range orderItems {
        if orderItem.Name == name {
            return &orderItem
        }
    }

    return nil
}
