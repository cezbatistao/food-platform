package domain

type OrderStatus struct {
    code string
}

var (
    CREATED    = OrderStatus{"CREATED"}
    PROCESSING = OrderStatus{"PROCESSING"}
    ACCEPTED   = OrderStatus{"ACCEPTED"}
    CANCELLED  = OrderStatus{"CANCELLED"}
    SHIPPED    = OrderStatus{"SHIPPED"}
    DELIVERED  = OrderStatus{"DELIVERED"}
)

func (e OrderStatus) GetOrderStatus() string {
    return e.code
}

func GetOrderStatusByCode(code string) *OrderStatus {
    if val, ok := orderStatusMap[code]; ok {
        return val
    }

    return nil
}

var (
    orderStatusMap = map[string]*OrderStatus{
        "CREATED"   : &CREATED,
        "PROCESSING": &PROCESSING,
        "ACCEPTED"  : &ACCEPTED,
        "CANCELLED" : &CANCELLED,
        "SHIPPED"   : &SHIPPED,
        "DELIVERED" : &DELIVERED,
    }
)

//const (
//    CREATED    OrderStatus = "CREATED"
//    PROCESSING             = "PROCESSING"
//    CANCELLED              = "CANCELLED"
//    SHIPPING               = "SHIPPING"
//    DELIVERED              = "DELIVERED"
//)
//var (
//    orderStatusMap = map[OrderStatus]string{
//        CREATED:    "CREATED",
//        PROCESSING: "PROCESSING",
//        CANCELLED:  "CANCELLED",
//        SHIPPING:   "SHIPPING",
//        DELIVERED:  "DELIVERED",
//        }
//)