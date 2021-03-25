package gateway

import (
    "context"
    "database/sql"
    "github.com/cezbatistao/food-platform/food-order/app/gateway/repository"
    "time"

	"github.com/cezbatistao/food-platform/food-order/app/domain"
	"github.com/google/uuid"
    "github.com/labstack/gommon/log"

    _ "github.com/lib/pq"
)

type OrderGateway interface {
    Save(order *domain.Order) (*domain.Order, error)
    GetByUuid(orderUuid *uuid.UUID) (*domain.Order, error)
    Update(order *domain.Order) (*domain.Order, error)
}

type OrderGatewayDatabase struct {
    ctx *context.Context
    db  *sql.DB
}

func NewOrderGateway(ctx *context.Context, db *sql.DB) *OrderGatewayDatabase {
    return &OrderGatewayDatabase{ctx: ctx, db: db}
}

func (g *OrderGatewayDatabase) Save(order *domain.Order) (*domain.Order, error) {
    order.DateCreated = time.Now()
    order.DateUpdated = time.Now()

    tx, err := g.db.BeginTx(*g.ctx, nil)
    checkErr(err)

    defer tx.Rollback()

    restaurantUuid := order.Restaurant.Uuid.String()

    lastInsertId := 0
    numberGenerated := 0
    tx.QueryRowContext(*g.ctx, `INSERT INTO tb_order(uuid, user_uuid, restaurant_uuid, restaurant_name, number, status, total, created_at, last_updated)
VALUES ($1, $2, $3, $4,(SELECT COALESCE(MAX(number)+1, 1) FROM tb_order WHERE restaurant_uuid = $5), $6, $7, $8, $9)
RETURNING id, number`,
        order.Uuid.String(), order.UserUuid.String(), restaurantUuid, order.Restaurant.Name,restaurantUuid,
         order.Status.GetOrderStatus(), order.Total, order.DateCreated, order.DateUpdated).Scan(&lastInsertId, &numberGenerated)
    checkErr(err)

    order.Id = lastInsertId
    order.Number = numberGenerated

    for index := range order.Items {
        orderItem := &order.Items[index]

        lastInsertId := 0
        tx.QueryRowContext(*g.ctx, `
INSERT INTO tb_order_item(order_id, uuid, menu_item_uuid, menu_item_name, amount, unit_value)
VALUES ($1, $2, $3, $4, $5, $6)
RETURNING id `,
            order.Id, orderItem.Uuid.String(), orderItem.MenuItemUuid.String(), orderItem.Name,
            orderItem.Amount, orderItem.UnitValue).Scan(&lastInsertId)
        checkErr(err)

        orderItem.Id = lastInsertId
    }

    // Commit the transaction.
    if err = tx.Commit(); err != nil {
        log.Errorf("error at save order: %+v", err)
        return nil, err
    }

	log.Infof("order created: %+v", order)

	return order, nil
}

func (g *OrderGatewayDatabase) GetByUuid(orderUuid *uuid.UUID) (*domain.Order, error) {
    tx, err := g.db.BeginTx(*g.ctx, nil)
    checkErr(err)

    defer tx.Rollback()

    rows, err := tx.QueryContext(*g.ctx, `
SELECT torder.id AS order_id,
       torder.uuid AS order_uuid,
       torder.user_uuid AS order_user_uuid,
       torder.restaurant_uuid AS order_restaurant_uuid,
       torder.restaurant_name AS order_restaurant_name,
       torder.number AS order_number,
       torder.status AS order_status,
       torder.total AS order_total,
       torder.created_at AS order_created_at,
       torder.last_updated AS order_last_updated,
       torderp.id AS payment_order_id,
       torderp.uuid AS payment_order_uuid,
       torderp.status AS payment_order_status,
       torderp.transaction_id AS payment_order_transaction_id,
       torderi.id AS order_item_id,
       torderi.uuid AS order_item_uuid,
       torderi.menu_item_uuid AS order_item_menu_item_uuid,
       torderi.menu_item_name AS order_item_menu_item_name,
       torderi.amount AS order_item_amount,
       torderi.unit_value AS order_item_unit_value
FROM tb_order AS torder
INNER JOIN tb_order_item AS torderi ON torderi.order_id  = torder.id
LEFT JOIN tb_order_payment AS torderp ON torderp.id  = torder.order_payment_id
WHERE torder.uuid = $1
ORDER BY torderi.id `,
        orderUuid.String())
    checkErr(err)

    var order *domain.Order
    for rows.Next() {
        var orderItem domain.OrderItem

        if order == nil {
            var orderStatusCode string
            order = new(domain.Order)

            var paymentOrderEntity = new(repository.PaymentOrderEntity)

            err = rows.Scan(&order.Id, &order.Uuid, &order.UserUuid, &order.Restaurant.Uuid, &order.Restaurant.Name,
                &order.Number, &orderStatusCode, &order.Total, &order.DateCreated, &order.DateUpdated, &paymentOrderEntity.Id,
                &paymentOrderEntity.Uuid, &paymentOrderEntity.Status, &paymentOrderEntity.TransactionId, &orderItem.Id, &orderItem.Uuid,
                &orderItem.MenuItemUuid, &orderItem.Name, &orderItem.Amount, &orderItem.UnitValue)
            checkErr(err)

            order.Status = *domain.GetOrderStatusByCode(orderStatusCode)
            if paymentOrderEntity.Status != nil {
                paymentUuid, _ := uuid.Parse(paymentOrderEntity.Uuid.String)

                order.Payment.Id = int(paymentOrderEntity.Id.Int32)
                order.Payment.Uuid = paymentUuid
                order.Payment.Status = *domain.GetPaymentStatusByCode(paymentOrderEntity.Status.String)
                order.Payment.TransactionId = paymentOrderEntity.TransactionId.String
            }
        } else {
            err = rows.Scan(&sql.NullInt32{}, &sql.NullString{}, &sql.NullString{}, &sql.NullString{}, &sql.NullString{},
                &sql.NullInt32{}, &sql.NullString{}, &sql.NullFloat64{}, &sql.NullTime{}, &sql.NullTime{}, &sql.NullInt32{},
                &sql.NullString{}, &sql.NullString{}, &sql.NullString{}, &orderItem.Id, &orderItem.Uuid, &orderItem.MenuItemUuid,
                &orderItem.Name, &orderItem.Amount, &orderItem.UnitValue)
            checkErr(err)
        }
        order.Items = append(order.Items, orderItem)
    }
    _ = rows.Close()

    // Commit the transaction.
    if err = tx.Commit(); err != nil {
        return nil, err
    }

    log.Infof("found order: +v", order)

    return order, nil
}

func (g *OrderGatewayDatabase) Update(order *domain.Order) (*domain.Order, error) {
    log.Infof("update order: %+v", order)

    tx, err := g.db.BeginTx(*g.ctx, nil)

    if order.Payment.Id == 0 {
        lastInsertId := 0
        tx.QueryRowContext(*g.ctx, `
INSERT INTO tb_order_payment(uuid, status, transaction_id)
VALUES ($1, $2, $3)
RETURNING id `,
            order.Payment.Uuid, order.Payment.Status.GetPaymentStatus(),
            order.Payment.TransactionId).Scan(&lastInsertId)
        checkErr(err)

        order.Payment.Id = lastInsertId
    }

    order.DateUpdated = time.Now()

    result, err := tx.ExecContext(*g.ctx, "UPDATE tb_order SET status = $1, order_payment_id = $2, last_updated = $3 WHERE id = $4",
        order.Status.GetOrderStatus(), order.Payment.Id, order.DateUpdated, order.Id)
    if err != nil {
        log.Fatal(err)
    }
    rows, err := result.RowsAffected()
    if err != nil {
        log.Fatal(err)
    }
    if rows != 1 {
        log.Fatalf("expected single row affected, got %d rows affected", rows)
    }

    if err = tx.Commit(); err != nil {
        return nil, err
    }

    return order, nil
}

func checkErr(err error) {
    if err != nil {
        panic(err.Error())
    }
}
