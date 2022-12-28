package domain

import "github.com/google/uuid"

type PaymentOrder struct {
    Id            int
    Uuid          uuid.UUID
    Status        PaymentStatus
    TransactionId string
}
