package gateway

import (
    "context"
    "encoding/json"
    "errors"
    "fmt"
    "strings"
    "time"

    "github.com/labstack/gommon/log"
    "github.com/segmentio/kafka-go"

    "github.com/cezbatistao/food-platform/food-order/app/config"
    "github.com/cezbatistao/food-platform/food-order/app/domain"
)

type DataEvent struct {
    Data interface{} `json:"data"`
}

type OrderCreatedEvent struct {
    Uuid           string           `json:"uuid"`
    UserUuid       string           `json:"user_uuid"`
    RestaurantUuid string           `json:"restaurant_uuid"`
    Total          string           `json:"total"`
    DateCreated    time.Time        `json:"date_created"`
    DateUpdated    time.Time        `json:"date_updated"`
    Items          []OrderItemEvent `json:"itens"`
}

type OrderItemEvent struct {
    Uuid         string `json:"uuid"`
    MenuItemUuid string `json:"menu_item_uuid"`
    Name         string `json:"name"`
    Amount       int    `json:"amount"`
    UnitValue string    `json:"unit_value"`
}

type OrderEvent struct {
    Uuid           string `json:"uuid"`
    UserUuid       string `json:"user_uuid"`
    RestaurantUuid string `json:"restaurant_uuid"`
    PaymentUuid    string `json:"payment_uuid"`
}

type OrderSendGateway interface {
    SendCreated(order *domain.Order) error
    SendProcessing(order *domain.Order) error
    SendCancelled(order *domain.Order) error 
}

type OrderSendGatewayKafka struct {
    ctx *context.Context
}

func NewOrderSendGateway(ctx *context.Context) *OrderSendGatewayKafka {
    return &OrderSendGatewayKafka{ctx: ctx}
}

func (g *OrderSendGatewayKafka) SendCreated(order *domain.Order) error {
	if order.Status != domain.CREATED {
		return errors.New(fmt.Sprintf("Order %s with status different from CREATED", order.Status))
	}

    topic := config.TopicOrderCreatedEvent()

    orderCreatedEvent := mapToOrderCreatedEvent(order)

    log.Infof("send created order %+v to topic %s", orderCreatedEvent, topic)
    return sendMessage(g.ctx, topic, order.Restaurant.Uuid.String(), orderCreatedEvent)
}

func (g *OrderSendGatewayKafka) SendProcessing(order *domain.Order) error {
    return sendProcessingOrCancelled(g.ctx, order, domain.PROCESSING, config.TopicOrderProcessingEvent())
}

func (g *OrderSendGatewayKafka) SendCancelled(order *domain.Order) error {
    return sendProcessingOrCancelled(g.ctx, order, domain.CANCELLED, config.TopicOrderCancelledEvent())
}

func sendProcessingOrCancelled(ctx *context.Context, order *domain.Order, orderStatus domain.OrderStatus, topic string) error {
    if orderStatus != domain.PROCESSING && orderStatus != domain.CANCELLED {
        return errors.New(fmt.Sprintf("order status may be PROCESSING or CANCELLED, couldn't accepted value %s", orderStatus))
    }
    if order.Status != orderStatus {
        return errors.New(fmt.Sprintf("order %s with status different from %s", order.Status, orderStatus))
    }

    orderEvent := mapToOrderEvent(order)
    log.Infof("send %s order event: %+v", strings.ToLower(orderStatus.GetOrderStatus()), orderEvent)

    _ = sendMessage(ctx, topic, orderEvent.RestaurantUuid, orderEvent)

    return nil
}

func sendMessage(ctx *context.Context, topic string, key string, bodyData interface{}) error {
    w := kafka.NewWriter(kafka.WriterConfig{
        Brokers: config.BootstrapServers(),
        Topic:   topic,
    })

    message, _ := json.Marshal(DataEvent{Data: bodyData})

    err := w.WriteMessages(*ctx, kafka.Message{
        Key: []byte(key),
        Value: message,
    })

    if err != nil {
        panic("could not write message " + err.Error())
    }

    return nil
}

func mapToOrderCreatedEvent(order *domain.Order) *OrderCreatedEvent {
    orderCreatedEvent := OrderCreatedEvent{Uuid: order.Uuid.String(), UserUuid: order.UserUuid.String(),
        RestaurantUuid: order.Restaurant.Uuid.String(), Total: fmt.Sprintf("%.2f", order.Total),
        DateCreated: order.DateCreated, DateUpdated: order.DateUpdated,
        Items: func(orderItems *[]domain.OrderItem) []OrderItemEvent {
            orderItemsEvent := make([]OrderItemEvent, 0)
            for _, orderItem := range *orderItems {
                orderItemsEvent = append(orderItemsEvent, OrderItemEvent{Uuid: orderItem.Uuid.String(),
                    MenuItemUuid: orderItem.MenuItemUuid.String(), Name: orderItem.Name,
                    Amount: orderItem.Amount, UnitValue: fmt.Sprintf("%.2f", orderItem.UnitValue)})
            }
            return orderItemsEvent
        }(&order.Items),
    }

    return &orderCreatedEvent
}

func mapToOrderEvent(order *domain.Order) *OrderEvent {
    orderEvent := OrderEvent{Uuid: order.Uuid.String(), UserUuid: order.UserUuid.String(),
        RestaurantUuid: order.Restaurant.Uuid.String(), PaymentUuid: order.Payment.Uuid.String()}

    return &orderEvent
}
