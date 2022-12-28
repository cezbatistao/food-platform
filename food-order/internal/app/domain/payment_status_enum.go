package domain

type PaymentStatus struct {
    code string
}

var (
    PAID    = PaymentStatus{code: "PAID"}
    REFUSED = PaymentStatus{"REFUSED"}
)
var (
    paymentStatusMap = map[string]*PaymentStatus{
        "PAID"   : &PAID,
        "REFUSED": &REFUSED,
    }
)

func (e PaymentStatus) GetPaymentStatus() string {
    return e.code
}

func GetPaymentStatusByCode(code string) *PaymentStatus {
    if val, ok := paymentStatusMap[code]; ok {
        return val
    }

    return nil
}
