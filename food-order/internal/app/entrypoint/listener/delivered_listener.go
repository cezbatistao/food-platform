package listener

import (
    "context"
    "encoding/json"

    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/usecase"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
    "github.com/segmentio/kafka-go"
)

type DeliveredEvent struct {
    UserUuid  string `json:"user_uuid"`
    OrderUuid string `json:"order_uuid"`
}

type DeliveredListener struct {
    updateOrderStatus *usecase.UpdateOrderStatus
}

func NewDeliveredListener(updateOrderStatus *usecase.UpdateOrderStatus) *DeliveredListener {
    return &DeliveredListener{updateOrderStatus: updateOrderStatus}
}

func (l *DeliveredListener) ConsumeEvent() {
    r := kafka.NewReader(kafka.ReaderConfig{
        Brokers: config.BootstrapServers(),
        Topic:   config.TopicOrderDeliveredEvent(),
        GroupID: config.TopicDeliveredEventGroupId(),
    })
    ctx := context.Background()
    for {
        // the `ReadMessage` method blocks until we receive the next event
        msg, err := r.ReadMessage(ctx)
        if err != nil {
            panic("could not read message " + err.Error())
        }

        var deliveredEvent DeliveredEvent
        json.Unmarshal(msg.Value, &deliveredEvent)

        log.Infof("listener delivered event: %+v", deliveredEvent)

        userUuid, _ := uuid.Parse(deliveredEvent.UserUuid)
        orderUuid, _ := uuid.Parse(deliveredEvent.OrderUuid)
        err = l.updateOrderStatus.Execute(ctx, &userUuid, &orderUuid, domain.DELIVERED)
        if err != nil {
            log.Errorf("error processing message at topic [%s]: %v", config.TopicOrderDeliveredEvent(), err)
        }
    }
}
