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
dotnet test test/test.csproj --logger:"junit;LogFilePath=TestResults/UnitTeststest-result.xml" --collect:"XPlat Code Coverage"
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

* default port restaurant app is `5000`

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
docker run -p 5000:80 food-order:1.0.0-[VERSION APP]
```

### Publish image
**TODO**

## Kubernetes
**TODO**
