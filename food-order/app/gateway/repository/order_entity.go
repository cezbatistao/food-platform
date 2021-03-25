package repository

import (
    "database/sql"
    "time"
)

type OrderEntity struct {
    Id             int
    Uuid           string
    Number         int
    Status         string
    UserUuid       string
    RestaurantUuid string
    RestaurantName string
    Payment        *PaymentOrderEntity
    Items          []OrderItemEntity
    Total          float64
    DateCreated    time.Time
    DateUpdated    time.Time
}

type PaymentOrderEntity struct {
    Id            *sql.NullInt32
    Uuid          *sql.NullString
    Status        *sql.NullString
    TransactionId *sql.NullString
}

type OrderItemEntity struct {
    Id           int
    Uuid         string
    MenuItemUuid string
    Name         string
    Amount       int
    UnitValue    float64
}