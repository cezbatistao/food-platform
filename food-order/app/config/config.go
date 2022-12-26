package config

import (
    "fmt"
    "os"

    "github.com/spf13/viper"
)

var appConfig config

type config struct {
	appPort                   int
    appName                   string
    appDescription            string
    appVersion                string
	logLevel                  string
    bootstrapServers          []string
    topicOrderCreatedEvent    string
    topicOrderProcessingEvent string
    topicOrderCancelledEvent  string
    topicPaymentEvent         string
    topicPaymentEventGroupId  string
    databaseName              string
    databaseUrl               string
    postgresDriver            string
    dirname                   string
    restaurantUrl             string
}

// Load load config
func Load() error {
    dirname, err := os.Getwd()
    if err != nil {
        panic(err)
    }

    viper.AddConfigPath(fmt.Sprintf("%s/config", dirname))
    viper.SetConfigName("app")
    viper.SetConfigType("env")

    viper.AutomaticEnv()
    viper.ReadInConfig()

    viper.SetDefault("PORT", 8080)
    viper.SetDefault("NAME", "food-order")
    viper.SetDefault("DESCRIPTION", "Order microservice description")
    viper.SetDefault("VERSION", "1.0.0")
    viper.SetDefault("ENVIRONMENT", "local")
    viper.SetDefault("LOG_LEVEL", "INFO")
    viper.SetDefault("BOOTSTRAP_SERVERS", []string{"localhost:9092"})
    viper.SetDefault("TOPIC_ORDER_CREATED_EVENT", "order-created-event")
    viper.SetDefault("TOPIC_ORDER_PROCESSING_EVENT", "order-processing-event")
    viper.SetDefault("TOPIC_ORDER_CANCELLED_EVENT", "order-cancelled-event")
    viper.SetDefault("TOPIC_PAYMENT_EVENT", "payment-event")
    viper.SetDefault("TOPIC_PAYMENT_EVENT_GROUP_ID", "PaymentOrderGroupId")
    viper.SetDefault("DATABASE_NAME", "db_order")
    viper.SetDefault("DATABASE_URL", "postgresql://postgresuser:postgrespasswd@localhost:5432/db_order?sslmode=disable")
    viper.SetDefault("POSTGRES_DRIVER", "postgres")
    viper.SetDefault("DIRNAME", dirname)
    viper.SetDefault("RESTAURANT_URL", "http://localhost:8882")

	appConfig = config{
		appPort: viper.GetInt("PORT"),
        appName: viper.GetString("NAME"),
        appDescription: viper.GetString("DESCRIPTION"),
        appVersion: viper.GetString("VERSION"),
		logLevel: viper.GetString("LOG_LEVEL"),
        bootstrapServers: viper.GetStringSlice("BOOTSTRAP_SERVERS"),
        topicOrderCreatedEvent: viper.GetString("TOPIC_ORDER_CREATED_EVENT"),
        topicOrderProcessingEvent: viper.GetString("TOPIC_ORDER_PROCESSING_EVENT"),
        topicOrderCancelledEvent: viper.GetString("TOPIC_ORDER_CANCELLED_EVENT"),
        topicPaymentEvent: viper.GetString("TOPIC_PAYMENT_EVENT"),
        topicPaymentEventGroupId: viper.GetString("TOPIC_PAYMENT_EVENT_GROUP_ID"),
        databaseUrl: viper.GetString("DATABASE_URL"),
        databaseName: viper.GetString("DATABASE_NAME"),
        postgresDriver: viper.GetString("POSTGRES_DRIVER"),
        dirname: viper.GetString("DIRNAME"),
        restaurantUrl: viper.GetString("RESTAURANT_URL"),
	}

	return nil
}

// Port return server port
func Port() int {
	return appConfig.appPort
}

func AppName() string {
    return appConfig.appName
}

func AppDescription() string {
    return appConfig.appDescription
}

func AppVersion() string {
    return appConfig.appVersion
}

func BootstrapServers() []string {
    return appConfig.bootstrapServers
}

func TopicOrderCreatedEvent() string {
    return appConfig.topicOrderCreatedEvent
}

func TopicOrderProcessingEvent() string {
    return appConfig.topicOrderProcessingEvent
}

func TopicOrderCancelledEvent() string {
    return appConfig.topicOrderCancelledEvent
}

func TopicPaymentEvent() string {
    return appConfig.topicPaymentEvent
}

func TopicPaymentEventGroupId() string {
    return appConfig.topicPaymentEventGroupId
}

func DatabaseUrl() string {
    return appConfig.databaseUrl
}

func DatabaseName() string {
    return appConfig.databaseName
}

func PostgresDriver() string {
    return appConfig.postgresDriver
}

func Dirname() string {
    return appConfig.dirname
}

func GetRestaurantUrl() string {
    return appConfig.restaurantUrl
}
