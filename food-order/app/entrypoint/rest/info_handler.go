package rest

import (
    "fmt"
    "github.com/swaggo/swag"
    "net"
    "net/http"
    "strings"

    "github.com/labstack/echo/v4"
	"github.com/spf13/viper"

    docs "github.com/cezbatistao/food-platform/food-order/docs/food_order"
)

type Info struct {
	Name        string `json:"name"`
	Description string `json:"description"`
	Version     string `json:"version"`
	Environment string `json:"environment"`
}

type InfoHTTPHandler struct {
}

func NewInfoHTTPHandler() *InfoHTTPHandler {
    return &InfoHTTPHandler{}
}

func (h *InfoHTTPHandler) GetInfo(c echo.Context) error {
	return c.JSON(http.StatusOK, &Info{
		Name:        viper.GetString("NAME"),
		Description: viper.GetString("DESCRIPTION"),
		Version:     viper.GetString("VERSION"),
		Environment: viper.GetString("ENVIRONMENT"),
	})
}

func (h *InfoHTTPHandler) GetDocJson(c echo.Context) error {
    schema := "http"
    host, port, _ := net.SplitHostPort(c.Request().Host)
    forwardedProto := c.Request().Header.Get("X-Forwarded-Proto")
    forwardedHost := c.Request().Header.Get("X-Forwarded-Host")
    forwardedPort := c.Request().Header.Get("X-Forwarded-Port")
    forwardedPrefix := c.Request().Header.Get("X-Forwarded-Prefix")
    forwardedSsl := c.Request().Header.Get("X-Forwarded-Ssl")
    forwardedFor := c.Request().Header.Get("X-Forwarded-For")

    fmt.Print("before modify")
    fmt.Printf("schema: %s\n", schema)
    fmt.Printf("host: %s\n", host)
    fmt.Printf("port: %s\n", port)

    fmt.Printf("forwardedProto: %s\n", forwardedProto)
    fmt.Printf("forwardedHost: %s\n", forwardedHost)
    fmt.Printf("forwardedPort: %s\n", forwardedPort)
    fmt.Printf("prefix: %s\n", forwardedPrefix)
    fmt.Printf("ssl: %s\n", forwardedSsl)
    fmt.Printf("for: %s\n", forwardedFor)

    schema = getFirstValueFromArrayOrDefault(forwardedProto, schema)
    host = getFirstValueFromArrayOrDefault(forwardedHost, host)
    port = getFirstValueFromArrayOrDefault(forwardedPort, port)

    if !strings.Contains(host, port) {
        host = fmt.Sprintf("%s:%s", host, port)
    }

    if forwardedSsl != "" && strings.EqualFold(forwardedSsl, "on") {
        schema = "https"
    }

    fmt.Print("after modify")
    fmt.Printf("schema: %s\n", schema)
    fmt.Printf("host: %s\n", host)
    fmt.Printf("port: %s\n", port)
    fmt.Printf("prefix: %s\n", forwardedPrefix)
    fmt.Printf("ssl: %s\n", forwardedSsl)
    fmt.Printf("for: %s\n", forwardedFor)

    docs.SwaggerInfo.Schemes = []string { schema }
    docs.SwaggerInfo.Host = fmt.Sprintf("%s%s", host, forwardedPrefix)

    doc, _ := swag.ReadDoc("swagger")
    c.Response().Writer.Write([]byte(doc))
    return nil
}

func getFirstValueFromArrayOrDefault(valuesInString string, defaultValue string) string {
    values := strings.Split(valuesInString, ",")
    if len(values) > 1 {
        return strings.TrimSpace(values[0])
    }

    return defaultValue
}
