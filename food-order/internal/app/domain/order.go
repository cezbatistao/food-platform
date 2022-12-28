package domain

import (
	"time"

	"github.com/google/uuid"
)

type Order struct {
	Id          int
	Uuid        uuid.UUID
    Number      int
	Status      OrderStatus
    UserUuid    uuid.UUID
	Restaurant  Restaurant
    Payment     PaymentOrder
	Items       []OrderItem
	Total       float64
	DateCreated time.Time
	DateUpdated time.Time
}

type OrderItem struct {
	Id           int
	Uuid         uuid.UUID
	MenuItemUuid uuid.UUID
	Name         string
	Amount       int
	UnitValue    float64
}
