package utils

import (
    "github.com/labstack/echo/v4"
)

type Error struct {
    Errors map[string]interface{} `json:"errors"`
}

func NewError(err error) Error {
    e := Error{}
    e.Errors = make(map[string]interface{})
    switch v := err.(type) {
    case *echo.HTTPError:
        e.Errors["body"] = v.Message
        default:
            e.Errors["body"] = v.Error()
    }
    return e
}

func NotFound(message string) Error {
    e := Error{}
    e.Errors = make(map[string]interface{})
    e.Errors["body"] = message
    return e
}

func HttpErrorResponse(message string) Error {
    e := Error{}
    e.Errors = make(map[string]interface{})
    e.Errors["body"] = message
    return e
}
