package rest

import (
    "net/http"
    "strconv"

    "github.com/google/uuid"
	"github.com/labstack/echo/v4"

    "github.com/cezbatistao/food-platform/food-order/internal/app/domain"
    "github.com/cezbatistao/food-platform/food-order/internal/app/usecase"

    "github.com/cezbatistao/food-platform/food-order/internal/app/entrypoint/rest/json"

    "github.com/cezbatistao/food-platform/food-order/internal/pkg/exceptions"
    "github.com/cezbatistao/food-platform/food-order/internal/pkg/utils"
)

type OrderRequest struct {
	RestaurantUuid string            `json:"restaurant_uuid"`
	Items          []MenuItemRequest `json:"items"`
}

type MenuItemRequest struct {
	MenuItemUuid string  `json:"menu_item_uuid"`
	Amount       int     `json:"amount"`
}

type OrderHTTPHandler struct {
	createOrderUseCase       *usecase.CreateOrder
    getOrdersFromUserUseCase *usecase.GetOrdersFromUser
    getOrderByUuidUseCase    *usecase.GetOrderByUuid
}

func NewOrderHTTPHandler(createOrder *usecase.CreateOrder, getOrdersFromUserUseCase *usecase.GetOrdersFromUser,
        getOrderByUuidUseCase *usecase.GetOrderByUuid) *OrderHTTPHandler {
    return &OrderHTTPHandler{createOrderUseCase: createOrder, getOrdersFromUserUseCase: getOrdersFromUserUseCase,
        getOrderByUuidUseCase: getOrderByUuidUseCase}
}

// CreateOrder godoc
// @Summary Create a food order.
// @Description Create a new food order.
// @Accept application/json
// @Consume application/json
// @Produce application/json
// @Param userUuid path string true  "User UUID"
// @Param order body OrderRequest true "New Order"
// @Success 200 {object} json.DataResponse{data=OrderResponse}
// @Router /api/v1/{userUuid}/orders [post]
func (h *OrderHTTPHandler) CreateOrder(c echo.Context) error {
    userUuidString := c.Param("userUuid")
    userUuid, _ := uuid.Parse(userUuidString)

	orderRequest := new(OrderRequest)
	if err := c.Bind(orderRequest); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}

    solicitationOfOrder := mapOrderRequestToSolicitation(&userUuid, orderRequest)

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

// GetOrders godoc
// @Summary Get all food orders from a user.
// @Description Get all food order from a user.
// @Accept application/json
// @Consume application/json
// @Produce application/json
// @Param userUuid path string true "User UUID"
// @Param page query string true "page number" default(0)
// @Param size query string true "size of page" default(10)
// @Success 200 {object} json.ListResponse{data=OrderResponse}
// @Router /api/v1/{userUuid}/orders [get]
func (h *OrderHTTPHandler) GetOrders(c echo.Context) error {
    userUuidString := c.Param("userUuid")
    userUuid, _ := uuid.Parse(userUuidString)

    pageString := c.QueryParam("page")
    page, _ := strconv.Atoi(pageString)

    sizeString := c.QueryParam("size")
    size, _ := strconv.Atoi(sizeString)

    r := c.Request()

    orderPageable := &domain.OrderPageable{UserUuid: userUuid, Page: page, Size: size}

    orderPage, _ := h.getOrdersFromUserUseCase.Execute(r.Context(), orderPageable)

    return c.JSON(http.StatusOK, &json.ListResponse{
        Data: func (orders []interface{}) []interface{} {
            ordersResponse := make([]interface{}, 0)
            for _, orderToCast := range orders {
                order := orderToCast.(domain.Order)
                ordersResponse = append(ordersResponse, mapOrderToOrderResponse(&order))
            }
            return ordersResponse
        }(orderPage.Content),
        Page: orderPage.Number,
        Size: orderPage.Size,
        TotalPages: orderPage.TotalPages,
        Total: orderPage.NumberOfElements,
    })
}

// GetOrderByUuid godoc
// @Summary Get a food order from a user.
// @Description Get a food order by order uuid from a user.
// @Accept application/json
// @Consume application/json
// @Produce application/json
// @Param userUuid path string true  "User UUID"
// @Param uuid path string true  "Order UUID"
// @Success 200 {object} json.DataResponse{data=OrderResponse}
// @Router /api/v1/{userUuid}/orders/{uuid} [get]
func (h *OrderHTTPHandler) GetOrderByUuid(c echo.Context) error {
    uuidString := c.Param("uuid")
    orderUuid, _ := uuid.Parse(uuidString)

    userUuidString := c.Param("userUuid")
    userUuid, _ := uuid.Parse(userUuidString)

    r := c.Request()

    order, err := h.getOrderByUuidUseCase.Execute(r.Context(), &userUuid, &orderUuid)
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

func mapOrderRequestToSolicitation(userUuid *uuid.UUID, orderRequest *OrderRequest) *domain.SolicitationOfOrder {
	restaurantUuid, _ := uuid.Parse(orderRequest.RestaurantUuid)

	solicitationOfOrder := domain.SolicitationOfOrder{
        UserUuid: *userUuid,
		RestaurantUuid: restaurantUuid,
		Items: func(itemsRequest *[]MenuItemRequest) []domain.SolicitationOrderItem {
			solicitationOrderItems := make([]domain.SolicitationOrderItem, len(*itemsRequest))
			for index, itemRequest := range *itemsRequest {
				uuid, _ := uuid.Parse(itemRequest.MenuItemUuid)
                solicitationOrderItems[index] = domain.SolicitationOrderItem{
                    MenuItemUuid: uuid, Amount: itemRequest.Amount}
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
        Items: func(orderItems *[]domain.OrderItem) *[]OrderItemResponse {
			orderItemsReponse := make([]OrderItemResponse, len(*orderItems))
			for index, orderItem := range *orderItems {
				orderItemsReponse[index] = OrderItemResponse{orderItem.Uuid.String(),
					orderItem.MenuItemUuid.String(), orderItem.Name, orderItem.Amount, orderItem.UnitValue}
			}
            if len(orderItemsReponse) > 0 {
                return &orderItemsReponse
            }

            return nil
		}(&order.Items),
    }

	return &orderResponse
}
