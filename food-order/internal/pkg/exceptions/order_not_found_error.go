package exceptions

import (
    "fmt"
    "github.com/google/uuid"
)

type OrderNotFoundError struct {
    OrderUuid uuid.UUID
}

func (e *OrderNotFoundError) Error() string {
    return fmt.Sprintf("order %s not found", e.OrderUuid)
}
