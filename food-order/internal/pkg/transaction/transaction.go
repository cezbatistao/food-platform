package transaction

import (
    "context"
    "database/sql"
    "fmt"

    "github.com/google/wire"
)

var (
    TransactionProviderSet = wire.NewSet(
        wire.Bind(new(Transaction), new(*transactionImpl)),
        NewTransaction,
    )
)

type Transaction interface {
    WithTransaction(
        ctx context.Context,
        fn func(ctxFn context.Context) error,
    ) error
}

type transactionImpl struct {
    db *sql.DB
}

func NewTransaction(db *sql.DB) *transactionImpl {
    return &transactionImpl{db: db}
}

func (t *transactionImpl) WithTransaction(
    ctx context.Context,
    fn func(ctxFn context.Context) error,
) (err error) {
    tx, err := t.db.BeginTx(ctx, &sql.TxOptions{})
    if err != nil {
        return err
    }

    defer func() {
        if p := recover(); p != nil {
            _ = tx.Rollback()
            panic(p)
        }
        if err != nil {
            if rbErr := tx.Rollback(); rbErr != nil {
                err = fmt.Errorf("tx err: %v, rb err: %v", err, rbErr)
            }
        } else {
            err = tx.Commit()
        }
    }()

    ctxTx := context.WithValue(ctx, "TxKey", tx)
    err = fn(ctxTx)
    return
}
