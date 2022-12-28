FROM golang:1.17-alpine AS build_base

RUN apk add --no-cache git && apk update && apk upgrade && apk add --no-cache make && apk add build-base

ENV GOPATH /go
# Set the Current Working Directory inside the container
WORKDIR /go/src/food-order

# We want to populate the module cache based on the go.{mod,sum} files.
COPY go.mod .
COPY go.sum .

RUN go mod download

COPY . .

# Build the Go app
RUN make build

# Start fresh from a smaller image
FROM alpine:3.9
RUN apk add ca-certificates

WORKDIR /app

COPY --from=build_base /go/src/food-order/out/food-order /app/food-order
COPY --from=build_base /go/src/food-order/db/migration /app/db/migration

# This container exposes port 8080 to the outside world
EXPOSE 8080

# Run the binary program produced by `go install`
CMD ["/app/food-order"]
