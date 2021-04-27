# Order App

## Application

Describe microservice here...

### Code template

We are using  
**TODO**

### VSCode plugin
**TODO**

#### Run tests and code coverage
```
dotnet test test/test.csproj --logger:"junit;LogFilePath=TestResults/UnitTeststest-result.xml" --collect:"XPlat Code Coverage" --settings:src/CodeCoverage.runsettings
```
\
Generate HTML Coverage Report:
```
reportgenerator "-reports:test/TestResults/{guuid}/coverage.cobertura.xml" "-targetdir:test/TestResults/coveragereport" -reporttypes:Html
```
\
Coverage HTML report: `test/TestResults/coveragereport/index.html`

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
dotnet run
```
Open [`http://localhost:5000/swagger/index.html`](http://localhost:5000/swagger/index.html) to use endpoints from application

* default port order app is `5000`

#### Build application
```
dotnet restore src
dotnet publish -c Release -o out src
```

## Docker

### Build image
* Needs execute command from: _Build application_
```
docker build -t food-order:1.0.0-[VERSION APP] .
```

### Run image
```
docker run -p 5000:5000 food-order:1.0.0-[VERSION APP]
```

### Publish image
```
docker tag food-order:1.0.0-[VERSION APP] cezbatistao/food-order:1.0.0-[VERSION APP]
```
In order to push you’ll first have to authenticate with the Docker Hub:
\
And push image to Docker Hub
```
docker push cezbatistao/food-order:1.0.0-[VERSION APP]
```

## Kubernetes
**TODO**
