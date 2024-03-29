package main

import (
    "context"
    "database/sql"
    "fmt"
    "os"
    "os/signal"
    "time"

    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
    "github.com/cezbatistao/food-platform/food-order/internal/app/config/database"
    "github.com/cezbatistao/food-platform/food-order/internal/app/config/routes"
    "github.com/cezbatistao/food-platform/food-order/internal/app/config/wire"

    _ "github.com/lib/pq"
    "github.com/sirupsen/logrus"
)

func main() {
    serverReady := make(chan bool)

    var db *sql.DB
    var err error

    config.Load()

    db, err = database.Init()
    if err != nil {
        panic(err.Error())
    }
    defer db.Close()

    e := routes.New(db)

    paymentListener := wire.InitializePaymentListener(db)
    go paymentListener.ConsumePaymentEvent()

    deliveredListener := wire.InitializeDeliveredListener(db)
    go deliveredListener.ConsumeEvent()

    go func() {
        if err := e.Start(fmt.Sprintf(":%d", config.Port())); err != nil {
            logrus.Errorf(err.Error())
            logrus.Infof("shutting down the server")
        }
    }()

    if serverReady != nil {
        serverReady <- true
    }

    quit := make(chan os.Signal)
    signal.Notify(quit, os.Interrupt)
    <-quit

    ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
    defer cancel()
    if err := e.Shutdown(ctx); err != nil {
        logrus.Fatalf("failed to gracefully shutdown the server: %s", err)
    }
}
