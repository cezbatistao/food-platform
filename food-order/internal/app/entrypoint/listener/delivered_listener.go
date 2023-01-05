package listener

import (
    "context"
    "encoding/json"

    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
    "github.com/cezbatistao/food-platform/food-order/internal/app/usecase"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
    "github.com/segmentio/kafka-go"
)

type DataEvent struct {
    Data DeliveredEvent `json:"data"`
}

type DeliveredEvent struct {
    UserUuid  string `json:"user_uuid"`
    OrderUuid string `json:"uuid"`
}

type DeliveredListener struct {
    processDeliveredOrder *usecase.ProcessDeliveredOrder
}

func NewDeliveredListener(processDeliveredOrder *usecase.ProcessDeliveredOrder) *DeliveredListener {
    return &DeliveredListener{processDeliveredOrder: processDeliveredOrder}
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

        var dataEvent DataEvent
        json.Unmarshal(msg.Value, &dataEvent)

        deliveredEvent := dataEvent.Data

        log.Infof("listener delivered event: %+v", deliveredEvent)

        userUuid, _ := uuid.Parse(deliveredEvent.UserUuid)
        orderUuid, _ := uuid.Parse(deliveredEvent.OrderUuid)
        err = l.processDeliveredOrder.Execute(ctx, &userUuid, &orderUuid)
        if err != nil {
            log.Errorf("error processing message at topic [%s]: %v", config.TopicOrderDeliveredEvent(), err)
        }
    }
}
