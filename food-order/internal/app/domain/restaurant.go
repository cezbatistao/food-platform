package domain

import "github.com/google/uuid"

type Restaurant struct {
	Uuid uuid.UUID
	Name string
}

type RestaurantDetail struct {
	Uuid  uuid.UUID
	Name  string
	Items []MenuItem
}

type MenuItem struct {
	Uuid      uuid.UUID
	Name      string
	UnitValue float64
}
