DB_URL ?= postgresql://postgresuser:postgrespasswd@localhost:5432/db_order?sslmode=disable
VERSION_APP ?= 0

download-mod:
	go mod download

download-install-dev-dependencies:
	go install github.com/google/wire/cmd/wire@latest && go install github.com/swaggo/swag/cmd/swag@latest

download-install-test-dependencies:
	 go install github.com/golang/mock/mockgen@v1.6.0 && go install github.com/jstemmer/go-junit-report@latest

download-install-all-dependencies:
	go mod download && go install github.com/google/wire/cmd/wire@latest && go install github.com/swaggo/swag/cmd/swag@latest && go install github.com/golang/mock/mockgen@v1.6.0 && go install github.com/jstemmer/go-junit-report@latest

build-swagger:
	CGO_ENABLED=0 swag init -g internal/app/config/routes/routes.go --output docs/food_order

gen-wire:
	CGO_ENABLED=0 cd internal/app/config/wire && wire

build-mocks:
	mockgen -source=internal/app/gateway/restaurant_gateway.go -destination=mocks/restaurant_gateway.go -package=mock
	mockgen -source=internal/app/gateway/order_gateway.go -destination=mocks/order_gateway.go -package=mock
	mockgen -source=internal/app/gateway/order_send_gateway.go -destination=mocks/order_send_gateway.go -package=mock

run-tests:
	CGO_ENABLED=0 go test ./internal/app/gateway/... ./internal/app/entrypoint/... ./internal/app/usecase/...

run-tests-junit-reports:
	CGO_ENABLED=0 go test -v 2>&1 ./internal/app/gateway/... ./internal/app/entrypoint/... ./internal/app/usecase/... | go-junit-report -set-exit-code > junit-report.xml

init-app: download-mod download-install-all-dependencies build-swagger gen-wire

init-build-app: download-install-dev-dependencies build-swagger gen-wire

build-app:
	go build -o ./out/food-order cmd/food-order/main.go

build: init-build-app build-app

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

server-run:
	go run main.go
