package rest

import (
    "net/http"

	"github.com/google/uuid"
	"github.com/labstack/echo/v4"

	"github.com/cezbatistao/food-platform/food-order/app/domain"
	"github.com/cezbatistao/food-platform/food-order/app/usecase"

    "github.com/cezbatistao/food-platform/food-order/app/entrypoint/rest/json"

    "github.com/cezbatistao/food-platform/food-order/pkg/exceptions"
    "github.com/cezbatistao/food-platform/food-order/pkg/utils"
)

type OrderRequest struct {
    UserUuid string                  `json:"user_uuid"`
	RestaurantUuid string            `json:"restaurant_uuid"`
	Items          []MenuItemRequest `json:"items"`
}

type MenuItemRequest struct {
	MenuItemUuid string  `json:"menu_item_uuid"`
	Amount       int     `json:"amount"`
	UnitValue    float64 `json:"unit_value"`
}

type OrderHTTPHandler struct {
	createOrderUseCase *usecase.CreateOrder
    getOrderByUuidUseCase *usecase.GetOrderByUuid
}

func NewOrderHTTPHandler(createOrder *usecase.CreateOrder, getOrderByUuidUseCase *usecase.GetOrderByUuid) *OrderHTTPHandler {
    return &OrderHTTPHandler{createOrderUseCase: createOrder, getOrderByUuidUseCase: getOrderByUuidUseCase}
}

// CreateOrder godoc
// @Summary Create a food order.
// @Description Create a new food order.
// @Accept application/json
// @Consume application/json
// @Produce application/json
// @Param order body OrderRequest true "New Order"
// @Success 200 {object} json.DataResponse{data=OrderResponse}
// @Router /api/v1/orders [post]
func (h *OrderHTTPHandler) CreateOrder(c echo.Context) error {
	orderRequest := new(OrderRequest)
	if err := c.Bind(orderRequest); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}

	solicitationOfOrder := mapOrderRequestToSolicitation(orderRequest)

    r := c.Request()

    order, err := h.createOrderUseCase.Execute(r.Context(), solicitationOfOrder)
    if err != nil {
        switch t := err.(type) {
            case *exceptions.RestaurantNotFoundError:
                return c.JSON(http.StatusNotFound, utils.NotFound(t.Error()))
            case *exceptions.MenuItemFromRestaurantNotFoundError:
                return c.JSON(http.StatusNotFound, utils.NotFound(t.Error()))
            default:
                return c.JSON(http.StatusInternalServerError, utils.NewError(err))
        }
    }

	orderResponse := mapOrderToOrderResponse(order)

	return c.JSON(http.StatusOK, &json.DataResponse{
		Data: orderResponse,
	})
}

// GetOrderByUuid godoc
// @Summary Get a food order.
// @Description Get a food order by order uuid.
// @Accept application/json
// @Consume application/json
// @Produce application/json
// @Param uuid path string true  "Order UUID"
// @Success 200 {object} json.DataResponse{data=OrderResponse}
// @Router /api/v1/orders/{uuid} [get]
func (h *OrderHTTPHandler) GetOrderByUuid(c echo.Context) error {
    uuidString := c.Param("uuid")
    orderUuid, _ := uuid.Parse(uuidString)

    r := c.Request()

    order, err := h.getOrderByUuidUseCase.Execute(r.Context(), &orderUuid)
    if err != nil {
        switch t := err.(type) { //t := err.(type)
            case *exceptions.RestaurantNotFoundError:
                return c.JSON(http.StatusNotFound, utils.NotFound(t.Error()))
            case *exceptions.MenuItemFromRestaurantNotFoundError:
                return c.JSON(http.StatusNotFound, utils.NotFound(t.Error()))
            default:
                return c.JSON(http.StatusInternalServerError, utils.NewError(err))
        }
    }

    orderResponse := mapOrderToOrderResponse(order)

    return c.JSON(http.StatusOK, &json.DataResponse{
        Data: orderResponse,
    })
}

func mapOrderRequestToSolicitation(orderRequest *OrderRequest) *domain.SolicitationOfOrder {
	userUuid, _ := uuid.Parse(orderRequest.UserUuid)
	restaurantUuid, _ := uuid.Parse(orderRequest.RestaurantUuid)

	solicitationOfOrder := domain.SolicitationOfOrder{
        UserUuid: userUuid,
		RestaurantUuid: restaurantUuid,
		Items: func(itemsRequest *[]MenuItemRequest) []domain.SolicitationOrderItem {
			solicitationOrderItems := make([]domain.SolicitationOrderItem, len(*itemsRequest))
			for index, itemRequest := range *itemsRequest {
				uuid, _ := uuid.Parse(itemRequest.MenuItemUuid)
                solicitationOrderItems[index] = domain.SolicitationOrderItem{
                    MenuItemUuid: uuid, Amount: itemRequest.Amount, UnitValue: itemRequest.UnitValue}
			}
			return solicitationOrderItems
		}(&orderRequest.Items),
	}

	return &solicitationOfOrder
}

func mapOrderToOrderResponse(order *domain.Order) *OrderResponse {
    var paymentOrderResponse *PaymentOrderResponse
    if (domain.PaymentOrder{}) != order.Payment {
        paymentOrderResponse = &PaymentOrderResponse{Uuid: order.Payment.Uuid.String(),
            Status: order.Payment.Status.GetPaymentStatus(), TransactionId: order.Payment.TransactionId}
    }
	orderResponse := OrderResponse{Uuid: order.Uuid.String(), UserUuid: order.UserUuid.String(),
        Number: order.Number, Status: order.Status.GetOrderStatus(), Total: order.Total,
        Restaurant: RestaurantResponse{order.Restaurant.Uuid.String(), order.Restaurant.Name},
        Payment: paymentOrderResponse,
        Items: func(orderItems *[]domain.OrderItem) []OrderItemResponse {
			orderItemsReponse := make([]OrderItemResponse, len(*orderItems))
			for index, orderItem := range *orderItems {
				orderItemsReponse[index] = OrderItemResponse{orderItem.Uuid.String(),
					orderItem.MenuItemUuid.String(), orderItem.Name, orderItem.Amount, orderItem.UnitValue}
			}
			return orderItemsReponse
		}(&order.Items),
    }

	return &orderResponse
}
