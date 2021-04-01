# Food Platform Kubernetes

## Minikube

### Start
```
minikube start --memory=4096
```

### Startup application
```
kubectl apply -f 6-create-databases.yaml
kubectl apply -f 7-application-startup.yaml
```

### Stop application
```
minikube stop
```
