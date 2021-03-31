# Restaurant UI App

## Application

Describe microservice here...
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
```
npm i
```
Then
```
npm run start
```

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

#### Build application
```
npm i
```
Then
```
npm run build
```

## Docker

### Build image
```
docker build -t food-ui-webapp:1.0.0-local .
```
* Needs execute command from: _Build application_

### Run image
```
docker run -p 8080:8080 -e PROXY_ENV=hml cezbatistao/food-ui-webapp:1.0.0-local
```

### Publish image
```
docker tag food-ui-webapp:1.0.0-local cezbatistao/food-ui-webapp:1.0.0-local
```
In order to push you’ll first have to authenticate with the Docker Hub:
```
docker login
<enter username & password>
``` 
Then
```
docker push cezbatistao/food-ui-webapp:1.0.0-local
```