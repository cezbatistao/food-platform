# diagram.py
from diagrams import Cluster, Diagram, Edge

from diagrams.k8s.clusterconfig import HPA
from diagrams.k8s.compute import Deployment, Pod, ReplicaSet
from diagrams.k8s.network import Ingress, Service

from diagrams.onprem.database import MongoDB
from diagrams.onprem.database import MySQL
from diagrams.onprem.database import PostgreSQL
from diagrams.onprem.database import Cassandra

from diagrams.onprem.queue import Kafka

from diagrams.custom import Custom

with Diagram("Food Platform Architecture", show=False):
    customUser = Custom("User", "./resources/notebook.png")

    orderWebUi = Pod("order web ui")

    ingWeb = Ingress()

    gatewayIng = Ingress("food gateway svc")
    gateway    = Pod("food gateway")
    gatewayIng >> gateway

    kafkaOrderCreatedEvt    = Kafka("order-created-event")
    kafkaOrderProcessingEvt = Kafka("order-processing-event")
    kafkaOrderCancelledEvt  = Kafka("order-cancelled-event")
    kafkaOrderDeliveredEvt  = Kafka("order-delivered-event")
    kafkaOrderShippedEvt    = Kafka("order-shipped-event")
    kafkaPaymentEvt         = Kafka("payment-event")

    orderDB  = PostgreSQL("order")
    orderApp = Pod("order app")
    orderSvc = Service("order svc")
    orderApp >> orderDB
    orderApp >> Edge(label="produce") >> kafkaOrderCreatedEvt
    orderApp >> Edge(label="produce") >> kafkaOrderProcessingEvt
    orderApp >> Edge(label="produce") >> kafkaOrderCancelledEvt
    orderApp >> Edge(label="produce") >> kafkaOrderShippedEvt
    orderApp >> Edge(label="produce") >> kafkaOrderDeliveredEvt
    orderSvc >> orderApp

    kafkaPaymentEvt << Edge(label="consume") << orderApp

    restaurantDB  = MySQL("restaurant")
    restaurantApp = Pod("restaurant app")
    restaurantSvc = Service("restaurant svc")
    restaurantApp >> restaurantDB
    restaurantSvc >> restaurantApp

    orderApp >> Edge(label="http") >> restaurantSvc

    ratingDB  = Cassandra("rating")
    ratingApp = Pod("rating app")
    ratingSvc = Service("rating svc")
    ratingApp >> ratingDB
    ratingSvc >> ratingApp

    reviewDB  = MongoDB("review")
    with Cluster("review app"):
        reviewApp1 = Pod("v1")
        reviewApp2 = Pod("v2")
        reviewApps = [reviewApp1, reviewApp2]
    reviewSvc  = Service("review svc")
    reviewApps >> reviewDB
    reviewSvc >> reviewApps
    
    reviewApp2 >> Edge(label="http")  >> ratingSvc

    kafkaOrderDeliveredEvt << Edge(label="consume") << reviewApps

    customUser >> ingWeb >> orderWebUi >> gatewayIng
    gateway >> Edge(label="http") >> orderSvc
    gateway >> Edge(label="http") >> restaurantSvc
    gateway >> Edge(label="http") >> reviewSvc
