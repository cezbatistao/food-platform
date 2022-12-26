package routes

import (
    "database/sql"
    "fmt"

    "github.com/cezbatistao/food-platform/food-order/app/config"
    "github.com/cezbatistao/food-platform/food-order/app/config/wire"
    docs "github.com/cezbatistao/food-platform/food-order/docs/food_order"

    "github.com/labstack/echo/v4"
    "github.com/labstack/echo/v4/middleware"

    echoSwagger "github.com/swaggo/echo-swagger"
)

// @title Swagger Food Order API
// @version 1.0
// @description Food Order API

// @host     localhost:8088
// @BasePath /
// @schemes  http https

// @produce	application/json
// @consumes application/json
func New(db *sql.DB) *echo.Echo {
    e := echo.New()

    e.HTTPErrorHandler = func(err error, c echo.Context) {
        // Take required information from error and context and send it to a service like New Relic
        fmt.Println(c.Path(), c.QueryParams(), err.Error())

        // Call the default handler to return the HTTP response
        e.DefaultHTTPErrorHandler(err, c)
    }

    // Middleware
    // e.Use(middleware.Logger())
    // e.Use(middleware.Recover())
     e.Use(middleware.CORS())

    docs.SwaggerInfo.Title = config.AppName()
    docs.SwaggerInfo.Description = config.AppDescription()
    docs.SwaggerInfo.Version = config.AppVersion()

    orderHTTPHandler := wire.InitializeOrderHTTPHandler(db)
    healthCheckHTTPHandler := wire.InitializeHealthCheckHTTPHandler()
    infoHTTPHandler := wire.InitializeInfoHTTPHandler()

    e.GET("/actuator/health", healthCheckHTTPHandler.GetHealth)
    e.GET("/actuator/info", infoHTTPHandler.GetInfo)
    e.GET("/v3/api-docs", infoHTTPHandler.GetDocJson)
    
    e.GET("/*", echoSwagger.EchoWrapHandler(echoSwagger.DocExpansion("list"), echoSwagger.DomID("swagger-ui"), echoSwagger.URL("/v3/api-docs")))

    e.POST("/api/v1/orders", orderHTTPHandler.CreateOrder)
    e.GET("/api/v1/orders/:uuid", orderHTTPHandler.GetOrderByUuid)

    return e
}
