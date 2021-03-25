## Component Test

### Running locally

#### Install Cypress dependencies
```
cd component-test
npm i
```

#### Running stubby
```
docker-compose up -d --build --force-recreate stubby
```

#### Running UI Weapp
```
cd ..
npm run start
```

#### Access application and mocks
- UI Webapp:
  - http://localhost:3000/
- Mocks:
  - http://localhost:8889/

#### Execute Cypress
- IDE Cypress, all `.features` : 
```
npm run test
```
- Headless Google Chrome, only sceneries with tag `@component` : 
```
npm run component-test:headless
```
