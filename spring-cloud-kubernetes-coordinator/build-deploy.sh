../gradlew clean :spring-cloud-kubernetes-coordinator:build
docker rmi -f coordinator:0.0.1
docker build -f Dockerfile -t coordinator:0.0.1 .
kubectl delete -f apisix-coordinator.yaml
kubectl apply -f apisix-coordinator.yaml