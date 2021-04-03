# Food Platform Kubernetes

## Minikube

### Start
```
minikube start --memory=4096
```

### Startup application
```
kubectl apply -f 6-create-databases.yaml
```

#### Access application
```
minikube ip
```
Returns a IP from minikube local machine, in my case: `192.168.99.100`
Open [`http://192.168.99.100:30080/`](http://192.168.99.100:30080/) to use application

### Stop application
```
minikube stop
```
