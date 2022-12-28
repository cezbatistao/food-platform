//go:build wireinject
// +build wireinject

package wire

import (
    "database/sql"
    "github.com/cezbatistao/food-platform/food-order/internal/app/entrypoint/listener"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/transaction"

    "github.com/cezbatistao/food-platform/food-order/internal/app/gateway"
    "github.com/cezbatistao/food-platform/food-order/internal/app/usecase"
    "github.com/cezbatistao/food-platform/food-order/internal/app/entrypoint/rest"

    "github.com/google/wire"
)

func InitializeOrderHTTPHandler(db *sql.DB) *rest.OrderHTTPHandler {
    panic(wire.Build(
        rest.NewOrderHTTPHandler,
        wire.Bind(new(gateway.OrderGateway), new(*gateway.OrderGatewayDatabase)),
        wire.Bind(new(gateway.OrderSendGateway), new(*gateway.OrderSendGatewayKafka)),
        wire.Bind(new(gateway.RestaurantGateway), new(*gateway.RestaurantGatewayHttp)),
        transaction.TransactionProviderSet,
        usecase.NewCreateOrder,
        usecase.NewGetOrdersFromUser,
        usecase.NewGetOrderByUuid,
        gateway.NewOrderGateway,
        gateway.NewOrderSendGateway,
        gateway.NewRestaurantGateway,
    ))
}

func InitializePaymentListener(db *sql.DB) *listener.PaymentListener {
    panic(wire.Build(
        listener.NewPaymentListener,
        wire.Bind(new(gateway.OrderGateway), new(*gateway.OrderGatewayDatabase)),
        wire.Bind(new(gateway.OrderSendGateway), new(*gateway.OrderSendGatewayKafka)),
        transaction.TransactionProviderSet,
        usecase.NewProcessOrderPayment,
        gateway.NewOrderGateway,
        gateway.NewOrderSendGateway,
    ))
}

func InitializeHealthCheckHTTPHandler() *rest.HealthCheckHTTPHandler {
    panic(wire.Build(
        rest.NewHealthCheckHTTPHandler,
    ))
}

func InitializeInfoHTTPHandler() *rest.InfoHTTPHandler {
    panic(wire.Build(
        rest.NewInfoHTTPHandler,
    ))
}
