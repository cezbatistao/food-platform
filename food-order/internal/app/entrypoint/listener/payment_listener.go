package listener

import (
    "context"
    "encoding/json"
    "errors"
    "fmt"
    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/usecase"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
    "github.com/segmentio/kafka-go"

    "github.com/cezbatistao/food-platform/food-order/internal/app/config"
)

type PaymentEvent struct {
    UserUuid      string `json:"user_uuid"`
    OrderUuid     string `json:"order_uuid"`
    Status        string `json:"status"`
    TransactionId string `json:"transaction_id"`
}

type PaymentListener struct {
    processOrderPayment *usecase.ProcessOrderPayment
}

func NewPaymentListener(processOrderPayment *usecase.ProcessOrderPayment) *PaymentListener {
    return &PaymentListener{processOrderPayment: processOrderPayment}
}

func (l *PaymentListener) ConsumePaymentEvent() {
    r := kafka.NewReader(kafka.ReaderConfig{
        Brokers: config.BootstrapServers(),
        Topic:   config.TopicPaymentEvent(),
        GroupID: config.TopicPaymentEventGroupId(),
    })
    ctx := context.Background()
    for {
        // the `ReadMessage` method blocks until we receive the next event
        msg, err := r.ReadMessage(ctx)
        if err != nil {
            panic("could not read message " + err.Error())
        }

        var paymentEvent PaymentEvent
        json.Unmarshal(msg.Value, &paymentEvent)

        log.Infof("listener payment event: %+v", paymentEvent)

        userUuid, orderUuid, paymentOrder, err := mapToPaymentOrder(&paymentEvent)
        if err != nil {
            log.Errorf("error processing message at topic [%s]: %v", config.TopicPaymentEvent(), err)
        } else {
            err = l.processOrderPayment.Execute(ctx, userUuid, orderUuid, paymentOrder)
            if err != nil {
                log.Errorf("error processing message at topic [%s]: %v", config.TopicPaymentEvent(), err)
            }
        }
    }
}

func mapToPaymentOrder(paymentEvent *PaymentEvent) (*uuid.UUID, *uuid.UUID, *domain.PaymentOrder, error) {
    userUuid, _ := uuid.Parse(paymentEvent.UserUuid)
    orderUuid, _ := uuid.Parse(paymentEvent.OrderUuid)
    paymentStatus := domain.GetPaymentStatusByCode(paymentEvent.Status)
    if paymentStatus == nil {
        log.Errorf("payment status %s doesn't exists", paymentEvent.Status)
        return nil, nil, nil, errors.New(fmt.Sprintf("payment status %s doesn't exists", paymentEvent.Status))
    }
    paymentOrder := domain.PaymentOrder{Status: *paymentStatus,
        TransactionId: paymentEvent.TransactionId}

    log.Infof("listener payment order: %+v", paymentOrder)
    return &userUuid, &orderUuid, &paymentOrder, nil
}
