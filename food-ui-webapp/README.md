# Restaurant UI App

## Application

Describe microservice here...

### Code template

We are using  
This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

#### Run tests and code coverage
**TODO**

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

##### Install dependencies
```
npm i
```
Download _node_modules_ dependencies

##### Run application
Runs the app in the development mode.
```
npm run start
```
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

#### Build application
* Needs execute command from: _Install dependencies_
```
REACT_APP_VERSION=[VERSION APP] npm run build
```

## Docker

### Build image
* Needs execute command from: _Build application_
```
docker build -t food-ui-webapp:1.0.0-[VERSION APP] .
```

### Run image
```
docker run -p 8080:8080 --net=host -e PROXY_ENV=hml cezbatistao/food-ui-webapp:1.0.0-[VERSION APP]
```

### Publish image
```
docker tag food-ui-webapp:1.0.0-[VERSION APP] cezbatistao/food-ui-webapp:1.0.0-[VERSION APP]
```
In order to push you’ll first have to authenticate with the Docker Hub:
```
docker login
<enter username & password>
```
\
And push image to Docker Hub
```
docker push cezbatistao/food-ui-webapp:1.0.0-[VERSION APP]
```

## Kubernetes
```
kubectl apply -f artifacts/deployment.yaml
kubectl apply -f artifacts/service.yaml
```
\
To access application: `minikube ip`, this return IP from my minikube local machine: `192.168.99.100`
\
Open [`http://192.168.99.100:30080/`](http://192.168.99.100:30080/) to use application
