package usecase

import (
    "context"
    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/exceptions"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"
    "github.com/google/uuid"

    "github.com/labstack/gommon/log"
)

type CreateOrder struct {
    orderGateway      gateway.OrderGateway
    orderSendGateway  gateway.OrderSendGateway
    restaurantGateway gateway.RestaurantGateway
    transaction       transaction.Transaction
}

func NewCreateOrder(orderGateway gateway.OrderGateway, orderSendGateway gateway.OrderSendGateway,
    restaurantGateway gateway.RestaurantGateway, transaction transaction.Transaction) *CreateOrder {
    return &CreateOrder{orderGateway: orderGateway, orderSendGateway: orderSendGateway,
        restaurantGateway: restaurantGateway, transaction: transaction}
    }

func (c *CreateOrder) Execute(ctx context.Context, solicitationOfOrder *domain.SolicitationOfOrder) (*domain.Order, error) {
    log.Infof("order requested: %+v", solicitationOfOrder)

    restaurantDetail, err := c.restaurantGateway.GetDetailByUuid(
        solicitationOfOrder.RestaurantUuid, getMenuItems(&solicitationOfOrder.Items))
    if err != nil {
        return nil, err
    }
    if restaurantDetail == nil {
        return nil, &exceptions.RestaurantNotFoundError{RestaurantUuid: solicitationOfOrder.RestaurantUuid}
    }

    mapMenuItems := toMapMenuItems(restaurantDetail.Items)
    orderItems, total := mapSolicitationOrderItemsToOrderItemsAndTotal(
        &solicitationOfOrder.Items, &mapMenuItems)

    order := domain.Order{Uuid: uuid.New(), UserUuid: solicitationOfOrder.UserUuid, Status: domain.CREATED, Total: total,
        Restaurant: domain.Restaurant{Uuid: restaurantDetail.Uuid, Name: restaurantDetail.Name}, Items: *orderItems}

    log.Infof("new order: %+v", order)

    var orderCreated *domain.Order
    err = c.transaction.WithTransaction(ctx, func (ctxTx context.Context) error {
        orderCreated, err = c.orderGateway.Save(ctxTx, &order)
        if err != nil {
            return err
        }

        c.orderSendGateway.SendCreated(ctx, orderCreated)

        return nil
    })

    return orderCreated, err
}

func getMenuItems(solicitationItems *[]domain.SolicitationOrderItem) *[]uuid.UUID {
    var menuItemsUuid []uuid.UUID
    for _, solicitationItem := range *solicitationItems {
        menuItemsUuid = append(menuItemsUuid, solicitationItem.MenuItemUuid)
    }

    return &menuItemsUuid
}

func toMapMenuItems(menuItems []domain.MenuItem) map[uuid.UUID]domain.MenuItem {
    mapMenuItems := make(map[uuid.UUID]domain.MenuItem, len(menuItems))
    for _, menuItem := range menuItems {
        mapMenuItems[menuItem.Uuid] = menuItem
    }

    return mapMenuItems
}

func mapSolicitationOrderItemsToOrderItemsAndTotal(solicitationOrderItems *[]domain.SolicitationOrderItem,
    mapMenuItems *map[uuid.UUID]domain.MenuItem) (*[]domain.OrderItem, float64) {

    orderItems := make([]domain.OrderItem, len(*solicitationOrderItems))

    var total float64

    for index, solicitationItem := range *solicitationOrderItems {
        menuItem := (*mapMenuItems)[solicitationItem.MenuItemUuid]

        orderItem := domain.OrderItem{
            Uuid        : uuid.New(),
            MenuItemUuid: menuItem.Uuid,
            Name        : menuItem.Name,
            Amount      : solicitationItem.Amount,
            UnitValue   : menuItem.UnitValue,
            }

            total += orderItem.UnitValue * float64(orderItem.Amount)

            orderItems[index] = orderItem
    }

    return &orderItems, total
}
