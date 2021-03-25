package gateway

import (
    "github.com/cezbatistao/food-platform/food-order/app/gateway/client"
    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
	"strconv"

	"github.com/cezbatistao/food-platform/food-order/app/domain"
	"github.com/google/uuid"
    "github.com/labstack/gommon/log"
)

type RestaurantGateway interface {
    GetByUuid(restaurantUuid uuid.UUID) (*domain.Restaurant, error)
    GetDetailByUuid(restaurantUuid uuid.UUID, menuItemsUuid *[]uuid.UUID) (*domain.RestaurantDetail, error)
}

type RestaurantGatewayHttp struct {
}

func NewRestaurantGateway() *RestaurantGatewayHttp {
    return &RestaurantGatewayHttp{}
}

func (g *RestaurantGatewayHttp) GetByUuid(restaurantUuid uuid.UUID) (*domain.Restaurant, error) {
	restaurant := domain.Restaurant{Uuid: restaurantUuid, Name: "Pizzaria Da Boa!"}

    log.Infof("restaurant founded: %+v", restaurant)

	return &restaurant, nil
}

func (g *RestaurantGatewayHttp) GetDetailByUuid(restaurantUuid uuid.UUID, menuItemsUuid *[]uuid.UUID) (*domain.RestaurantDetail, error) {
    restaurantResponse, err := client.GetById(restaurantUuid)
    if err != nil {
        log.Errorf("error when request restaurant with uuid %s", restaurantUuid)
        return nil, err
    }

    menuItems, menuItemsNotFound := getMenuItemsFromResponse(&restaurantResponse.Items, menuItemsUuid)

    if len(*menuItemsNotFound) > 0 {
        log.Errorf("menu items [%s] not found at resturant %s", menuItemsUuid, restaurantUuid)
        return nil, &exceptions.MenuItemFromRestaurantNotFoundError{RestaurantUuid: restaurantUuid, MenuItems: *menuItemsNotFound}
	}

	restaurantDetail := &domain.RestaurantDetail{Uuid: restaurantUuid,
        Name: restaurantResponse.Name, Items: *menuItems}

    log.Infof("restaurant founded: %+v", restaurantDetail)

	return restaurantDetail, nil
}

func getMenuItemsFromResponse(menuItemsResponse *[]client.MenuItemResponse, menuItemsUuid *[]uuid.UUID) (*[]domain.MenuItem, *[]uuid.UUID) {
    menuItems := make([]domain.MenuItem, 0)
    menuItemsNotFound := make([]uuid.UUID, 0)
    for _, menuItemUuid := range *menuItemsUuid {
        found := false
        for _, menuItemResponse := range *menuItemsResponse {
            if menuItemUuid.String() == menuItemResponse.Uuid {
                unitValue, _ := strconv.ParseFloat(menuItemResponse.UnitValue, 64)
                menuItems = append(menuItems, domain.MenuItem{Uuid: menuItemUuid,
                    Name: menuItemResponse.Name, UnitValue: unitValue})
                found = true
                break
            }
        }

        if !found {
            menuItemsNotFound = append(menuItemsNotFound, menuItemUuid)
        }

        if len(menuItems) == len(*menuItemsUuid) {
            break
        }
    }

    return &menuItems, &menuItemsNotFound
}
