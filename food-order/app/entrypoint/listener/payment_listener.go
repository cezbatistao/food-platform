package listener

import (
    "context"
    "encoding/json"
    "errors"
    "fmt"
    "github.com/cezbatistao/food-platform/food-order/app/domain"
    "github.com/cezbatistao/food-platform/food-order/app/usecase"

    "github.com/google/uuid"
    "github.com/labstack/gommon/log"
    "github.com/segmentio/kafka-go"

    "github.com/cezbatistao/food-platform/food-order/app/config"
)

type PaymentEvent struct {
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

func (l *PaymentListener) ConsumePaymentEvent(ctx context.Context) {
    r := kafka.NewReader(kafka.ReaderConfig{
        Brokers: config.BootstrapServers(),
        Topic:   config.TopicPaymentEvent(),
        GroupID: config.TopicPaymentEventGroupId(),
    })
    for {
        // the `ReadMessage` method blocks until we receive the next event
        msg, err := r.ReadMessage(ctx)
        if err != nil {
            panic("could not read message " + err.Error())
        }

        var paymentEvent PaymentEvent
        json.Unmarshal(msg.Value, &paymentEvent)

        log.Infof("listener payment event: %+v", paymentEvent)

        orderUuid, paymentOrder, err := mapToPaymentOrder(&paymentEvent)
        if err != nil {
            log.Errorf("error processing message at topic [%s]: %v", config.TopicPaymentEvent(), err)
        } else {
            err = l.processOrderPayment.Execute(orderUuid, paymentOrder)
            if err != nil {
                log.Errorf("error processing message at topic [%s]: %v", config.TopicPaymentEvent(), err)
            }
        }
    }
}

func mapToPaymentOrder(paymentEvent *PaymentEvent) (*uuid.UUID, *domain.PaymentOrder, error) {
    orderUuid, _ := uuid.Parse(paymentEvent.OrderUuid)
    paymentStatus := domain.GetPaymentStatusByCode(paymentEvent.Status)
    if paymentStatus == nil {
        log.Errorf("payment status %s doesn't exists", paymentEvent.Status)
        return nil, nil, errors.New(fmt.Sprintf("payment status %s doesn't exists", paymentEvent.Status))
    }
    paymentOrder := domain.PaymentOrder{Status: *paymentStatus,
        TransactionId: paymentEvent.TransactionId}

    log.Infof("listener payment order: %+v", paymentOrder)
    return &orderUuid, &paymentOrder, nil
}
