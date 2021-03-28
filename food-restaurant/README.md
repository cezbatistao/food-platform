# Restaurant App

## application

Describe microservice here...

### Code template

We are using
TODO

### IntelliJ plugin
TODO

#### Run tests and code coverage
```
./gradlew clean test
```

#### Run components tests
TODO

#### Setup application
TODO

#### Start application
```
./gradlew  bootRun
```

Open [`http://localhost:8181/swagger-ui/`](http://localhost:8081/swagger-ui/) to use endpoints from application

* default port restaurant app is `8181`

## docker

### build image
```
./gradlew clean build docker -x test
```

### publish image
```
./gradlew dockerTagDockerHub
```
In order to push you’ll first have to authenticate with the Docker Hub:
```
docker login
<enter username & password>
``` 
Then
```
./gradlew dockerPushDockerHub
```