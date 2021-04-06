# Food Platform Kubernetes

## Minikube

### Start
```
minikube start --memory=4096
```

### Startup application
```
kubectl apply -f 1-istio-init.yaml
kubectl apply -f 2-istio-configuration-minikube.yaml
kubectl apply -f 3-istio-addons-kiali-prometheus-grafana-jaeger.yaml
kubectl apply -f 4-kiali-secret.yaml
kubectl apply -f 5-label-default-namespace.yaml
kubectl apply -f 6-create-databases.yaml
```
\
For now we use only the last command: `kubectl apply -f 6-create-databases.yaml`

#### Access cluster
```
minikube ip
```
Returns a IP from minikube local machine, in my case: `192.168.99.100`

##### Istio tools

- Kiali: [http://192.168.99.100:31000/](http://192.168.99.100:31000/)
- Jaeger: [http://192.168.99.100:31001/](http://192.168.99.100:31001/)
- Grafana: [http://192.168.99.100:31002/](http://192.168.99.100:31002/)

### Stop application
```
minikube stop
```
