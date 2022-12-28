DB_URL ?= postgresql://postgresuser:postgrespasswd@localhost:5432/db_order?sslmode=disable
VERSION_APP ?= 0

init-app:
	go mod download && go get -u github.com/swaggo/swag/cmd/swag && go get -u github.com/google/wire/cmd/wire

swagger:
	swag init -g app/config/routes/routes.go --output docs/food_order

wire:
	cd app/config/wire && wire

test-mockgen:
	go install github.com/golang/mock/mockgen@v1.6.0
	mockgen -source=app/gateway/restaurant_gateway.go -destination=app/gateway/mock/restaurant_gateway.go -package=mock
	mockgen -source=app/gateway/order_gateway.go -destination=app/gateway/mock/order_gateway.go -package=mock
	mockgen -source=app/gateway/order_send_gateway.go -destination=app/gateway/mock/order_send_gateway.go -package=mock

run-test:
	pwd
	go test ./app/gateway/... ./app/entrypoint/... ./app/usecase/...

test-generate-report:
	go install github.com/jstemmer/go-junit-report@latest
	go test -v 2>&1 ./app/gateway/... ./app/entrypoint/... ./app/usecase/... | go-junit-report -set-exit-code > junit-report.xml

build-dev: init-app swagger wire

build-app:
	go build -o ./out/food-order .

build: build-dev build-app

build-dockerfile:
	docker build -t food-order:1.0.0-${VERSION_APP} .

build-push:
	docker tag food-order:1.0.0-${VERSION_APP} cezbatistao/food-order:1.0.0-${VERSION_APP}
	docker push cezbatistao/food-order:1.0.0-${VERSION_APP}

migrateup:
	migrate -path db/migration -database "$(DB_URL)" -verbose up

migrateup1:
	migrate -path db/migration -database "$(DB_URL)" -verbose up 1

migratedown:
	migrate -path db/migration -database "$(DB_URL)" -verbose down

migratedown1:
	migrate -path db/migration -database "$(DB_URL)" -verbose down 1

server:
	go run main.go

server-run: swagger wire server
