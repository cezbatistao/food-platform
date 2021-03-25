# Order App

## Application

Describe microservice here...

### Code template

We are using
**TODO**

### VSCode plugin
**TODO**

#### Run tests
```
make test-mockgen
make test
```

#### Run tests and code coverage
```
make test-mockgen
make test-generate-report
```
\
Test HTML report: `test_report.html`
Coverage HTML report: `coverage.html`

#### Run components tests
**TODO**

#### Setup application
Go on folder `dependencies/` and start _docker-compose_
```
docker-compose up -d --build
```
\
To turn off
```
docker-compose down
```

#### Start application
* Needs execute command from: _Setup application_ start _docker-compose_
```
make build-dev
go run main.go
```
Open [`http://localhost:8088/swagger-ui/index.html`](http://localhost:8088/swagger-ui/index.html) to use endpoints from application

* default port order app is `8088`

#### Build application
**TODO**

## Docker

### Build image
* Needs execute command from: _Build application_
```
VERSION_APP=[VERSION APP] make build-dockerfile
```

### Run image
```
docker run --network="host" food-order:1.0.0-[VERSION APP]
```

### Publish image
```
docker tag food-order:1.0.0-[VERSION APP] cezbatistao/food-order:1.0.0-[VERSION APP]
```
For push at docker hub will need first authenticate with the Docker Hub:
\
And push image to Docker Hub
```
docker push cezbatistao/food-order:1.0.0-[VERSION APP]
```

## Kubernetes
Install
```bash
cd artifacts
helm upgrade food-order --install . --namespace default --wait --timeout 300s --force -f values.yaml -f values-minikube.yaml --set 'image.tag=1.0.0-[VERSION APP]'
```

Uninstall
```
helm uninstall food-restaurant --namespace default --wait --timeout 300s
```
