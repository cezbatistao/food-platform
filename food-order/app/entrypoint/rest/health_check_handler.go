package rest

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

type Health struct {
	Status string `json:"status"`
}

type HealthCheckHTTPHandler struct {
}

func NewHealthCheckHTTPHandler() *HealthCheckHTTPHandler {
    return &HealthCheckHTTPHandler{}
}

func (h *HealthCheckHTTPHandler) GetHealth(c echo.Context) error {
	return c.JSON(http.StatusOK, &Health{
		Status: "UP",
	})
}
