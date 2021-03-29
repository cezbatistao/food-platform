# diagram.py
from diagrams import Cluster, Diagram

from diagrams.k8s.clusterconfig import HPA
from diagrams.k8s.compute import Deployment, Pod, ReplicaSet
from diagrams.k8s.network import Ingress, Service

from diagrams.onprem.database import MongoDB
from diagrams.onprem.database import MySQL
from diagrams.onprem.inmemory import Redis

from diagrams.custom import Custom

with Diagram("Food Platform Architecture", show=False):
    customUser = Custom("User", "./resources/notebook.png")

    orderWebUi = Pod("order web ui")

    ing = Ingress()

    gatewaySvc = Service("food gateway svc")
    gateway    = Pod("food gateway")

    orderDB  = MySQL("order")
    orderSvc = Service("order svc")
    orderApp = Pod("order app")
    orderApp >> orderDB
    orderSvc >> orderApp

    restaurantDB  = MySQL("restaurant")
    restaurantSvc = Service("restaurant svc")
    restaurantApp = Pod("restaurant app")
    restaurantApp >> restaurantDB
    restaurantSvc >> restaurantApp

    reviewDB  = MongoDB("review")
    reviewSvc = Service("review svc")
    with Cluster("review app"):
        reviewApp1 = Pod("v1")
        reviewApp2 = Pod("v2")
        reviewApps = [reviewApp1, reviewApp2]
    reviewApps >> reviewDB

    ratingDB  = Redis("rating")
    ratingSvc = Service("rating svc")
    ratingApp = Pod("rating app")
    ratingApp >> ratingDB

    gatewaySvc >> gateway
    reviewSvc  >> reviewApps
    ratingSvc  >> ratingApp

    customUser >> orderWebUi >> ing >> gatewaySvc
    gateway    >> orderSvc
    gateway    >> restaurantSvc
    orderApp   >> reviewSvc
    orderApp   >> restaurantSvc
    reviewApp2 >> ratingSvc
