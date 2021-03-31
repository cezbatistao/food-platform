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
Open [`http://localhost:8181/swagger-ui/`](http://localhost:8181/swagger-ui/) to use endpoints from application

* default port restaurant app is `8181`

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
docker run -p 8181:8181 --net=host food-restaurant:[VERSION APP]
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
