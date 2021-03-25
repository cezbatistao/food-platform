package rest

type OrderResponse struct {
	Uuid       string                `json:"uuid"`
	UserUuid   string                `json:"user_uuid"`
    Number     int                   `json:"number"`
    Status     string                `json:"status"`
	Restaurant RestaurantResponse    `json:"restaurant"`
    Payment    *PaymentOrderResponse `json:"payment,omitempty"`
	Items      []OrderItemResponse   `json:"items"`
	Total      float64               `json:"total"`
}

type OrderItemResponse struct {
	Uuid         string  `json:"uuid"`
	MenuItemUuid string  `json:"menu_item_uuid"`
	Name         string  `json:"name"`
	Amount       int     `json:"amount"`
	UnitValue    float64 `json:"unitValue"`
}

type RestaurantResponse struct {
	Uuid string `json:"uuid"`
	Name string `json:"name"`
}

type PaymentOrderResponse struct {
    Uuid          string `json:"uuid"`
    Status        string `json:"status"`
    TransactionId string `json:"transaction_id"`
}
