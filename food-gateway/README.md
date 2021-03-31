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
./gradlew  bootRun
```
Open [`http://localhost:9090/swagger-ui/`](http://localhost:9090/swagger-ui/) to use endpoints from application

* default port gateway app is `9090`

## Docker

### Build image
```
./gradlew clean build docker -x test -Pbuild.number=[VERSION APP]
```

### Run image
* Needs execute command from: _Setup application_ start _docker-compose_
```
docker run -p 9090:9090 --net=host food-gateway:[VERSION APP]
```

### Publish image
```
./gradlew dockerTagDockerHub
```
In order to push you’ll first have to authenticate with the Docker Hub:
```
docker login
<enter username & password>
``` 
\
And push image to Docker Hub
```
./gradlew dockerPushDockerHub
```
