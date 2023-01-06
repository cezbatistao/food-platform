package client

import (
    "encoding/json"
    "errors"
    "fmt"
    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/exceptions"
    "net/http"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
)

type DataResponse struct {
    Data RestaurantResponse `json:"data"`
}

type RestaurantResponse struct {
    Uuid  string             `json:"uuid"`
    Name  string             `json:"name"`
    Items []MenuItemResponse `json:"items"`
}

type MenuItemResponse struct {
    Uuid      string `json:"uuid"`
    Name      string `json:"name"`
    UnitValue string `json:"value"`
}

func GetById(restaurantUuid uuid.UUID) (*RestaurantResponse, error) {
    URL := fmt.Sprintf("%s/api/v1/restaurants/%s", config.GetRestaurantUrl(), restaurantUuid.String())

    client := &http.Client{}
    req, err := http.NewRequest(http.MethodGet, URL, nil)
    if err != nil {
        log.Fatal(err)
    }

    q := req.URL.Query()
    //	for _, menuItemUuid := range *menuItemsUuid {
    //		q.Add("menu_item_uuid", menuItemUuid.String())
    //	}

    req.URL.RawQuery = q.Encode()

    resp, err := client.Do(req)
    if err != nil {
        log.Errorf("error when sending restaurant request")
        return nil, errors.New("error when sending restaurant request")
    }

    defer resp.Body.Close()

    if resp.StatusCode == 404 {
        log.Errorf("cannot get restaurant: %s", restaurantUuid)
        return nil, &exceptions.RestaurantNotFoundError{RestaurantUuid: restaurantUuid}
    }

    dataResponse := new(DataResponse)
    if err := json.NewDecoder(resp.Body).Decode(dataResponse); err != nil {
        log.Fatal("ooopsss! an error occurred, please try again")
    }

    return &dataResponse.Data, nil
}
