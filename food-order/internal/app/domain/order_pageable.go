package domain

import "github.com/google/uuid"

type OrderPageable struct {
    UserUuid uuid.UUID
    Page     int
    Size     int
}
