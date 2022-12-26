package mock

import (
    "context"
    "database/sql"
)
type transactionMock struct {
}

func NewTransactionMock() *transactionMock {
    return &transactionMock{}
}

func (t *transactionMock) WithTransaction(
    ctx context.Context,
    fn func(ctx context.Context, tx *sql.Tx) error,
) (err error) {
    err = fn(ctx, nil)
    return
}
