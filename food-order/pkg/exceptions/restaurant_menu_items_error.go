package exceptions

import (
    "fmt"
    "github.com/google/uuid"
)

type MenuItemFromRestaurantNotFoundError struct {
    MenuItems []uuid.UUID
    RestaurantUuid uuid.UUID
}

func (e *MenuItemFromRestaurantNotFoundError) Error() string {
    return fmt.Sprintf("menu items [%s] from restaurant %s not found", e.MenuItems, e.RestaurantUuid)
}
