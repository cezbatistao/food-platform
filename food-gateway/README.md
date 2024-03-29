# Gateway App

## Application

Describe microservice here...

### Code template

We are using  
**TODO**

### IntelliJ plugin
**TODO**

#### Run tests and code coverage
```
./gradlew clean test
```

#### Run components tests
**TODO**

#### Setup application
Go on folder `dependencies/` and start _docker-compose_
```
docker-compose up -d --build
```
\
To turn off
```bash
docker-compose down
```

#### Start application
* Needs execute command from: _Setup application_ start _docker-compose_
```
./gradlew bootRun
```
Open [`http://localhost:9090/swagger-ui.html`](http://localhost:9090/swagger-ui.html) to use endpoints from application

* default port gateway app is `9090`

#### Build application
```
./gradlew clean build -x test -Pbuild.number=[VERSION APP]
```

## Docker

### Build image
* Needs execute command from: _Build application_
```
./gradlew docker -Pbuild.number=[VERSION APP]
```

### Run image
* Needs execute command from: _Setup application_ start _docker-compose_
```
docker run -p 9090:9090 food-gateway:[VERSION APP]
```

### Publish image
```
./gradlew dockerTagDockerHub -Pbuild.number=[VERSION APP]
```
In order to push you’ll first have to authenticate with the Docker Hub:
```
docker login
<enter username & password>
```
\
And push image to Docker Hub
```
./gradlew dockerPushDockerHub -Pbuild.number=[VERSION APP]
```

## Kubernetes

Install
```
cd artifacts
helm upgrade food-gateway --install . --namespace default --wait --timeout 300s --force -f values.yaml -f values-minikube.yaml --set 'image.tag=1.0.0-[VERSION APP]'
```

Uninstall
```
helm uninstall food-gateway --namespace default --wait --timeout 300s
```
