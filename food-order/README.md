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
dotnet test test/test.csproj
```

#### Run components tests
**TODO**

#### Setup application
```
dotnet test test/test.csproj --logger:"junit;LogFilePath=TestResults/UnitTeststest-result.xml" --collect:"XPlat Code Coverage"
```
\
Generate HTML Coverage Report:
```
reportgenerator "-reports:test/TestResults/$1/coverage.cobertura.xml" "-targetdir:test/TestResults/coveragereport" -reporttypes:Html
```
\
Coverage HTML report: `test/TestResults/coveragereport/index.html`

#### Start application
* Needs execute command from: _Setup application_ start _docker-compose_
```
dotnet run
```
Open [`http://localhost:5000/swagger/index.html`](http://localhost:5000/swagger/index.html) to use endpoints from application

* default port restaurant app is `5000`

#### Build application
**TODO**

## Docker

### Build image
* Needs execute command from: _Build application_
**TODO**

### Run image
**TODO**

### Publish image
**TODO**

## Kubernetes
**TODO**
