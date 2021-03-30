# Restaurant App

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
Go on folder `dependencies/`
```
docker-compose up -d --build
```
\
To turn off
```bash
docker-compose down
```

#### Start application
```
./gradlew  bootRun
```

Open [`http://localhost:8181/swagger-ui/`](http://localhost:8081/swagger-ui/) to use endpoints from application

* default port restaurant app is `8181`

## Docker

### Build image
```
./gradlew clean build docker -x test
```
* Needs execute command from: _Setup application_

### Run image
```
docker run -p 8181:8181 --net=host cezbatistao/food-restaurant:1.0.0-local
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
Then
```
./gradlew dockerPushDockerHub
```
