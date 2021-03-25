package exceptions

import (
    "fmt"
    "github.com/google/uuid"
)

type RestaurantNotFoundError struct {
    RestaurantUuid uuid.UUID
}

func (e *RestaurantNotFoundError) Error() string {
    return fmt.Sprintf("restaurant %s not found", e.RestaurantUuid)
}
