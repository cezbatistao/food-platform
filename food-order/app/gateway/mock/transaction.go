package mock

import (
    "context"
)
type transactionMock struct {
}

func NewTransactionMock() *transactionMock {
    return &transactionMock{}
}

func (t *transactionMock) WithTransaction(
    ctx context.Context,
    fn func(ctxTx context.Context) error,
) (err error) {
    err = fn(ctx)
    return
}
