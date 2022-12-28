package domain

import "github.com/google/uuid"

type SolicitationOfOrder struct {
    UserUuid       uuid.UUID
    RestaurantUuid uuid.UUID
    Items          []SolicitationOrderItem
}

type SolicitationOrderItem struct {
    MenuItemUuid uuid.UUID
    Amount       int
}
