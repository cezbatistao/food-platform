package transaction

import (
    "context"
    "database/sql"
    "fmt"
    "github.com/google/wire"
)

var (
    TransactionProviderSet = wire.NewSet(
        wire.Bind(new(Transaction), new(*transactionDb)),
        NewTransaction,
    )
)

type Transaction interface {
    WithTransaction(
        ctx context.Context,
        fn func(ctx context.Context, tx *sql.Tx) error,
    ) error
}

type transactionDb struct {
    db *sql.DB
}

func NewTransaction(db *sql.DB) *transactionDb {
    return &transactionDb{db: db}
}

func (t *transactionDb) WithTransaction(
    ctx context.Context,
    fn func(ctx context.Context, tx *sql.Tx) error,
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

    err = fn(ctx, tx)
    return
}





// TODO find another place to store this code!
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
