../gradlew clean :example-service-uaa:build
docker rmi -f uaa:0.0.1
docker build -f Dockerfile -t uaa:0.0.1 .
kubectl delete -f uaa.yaml
kubectl apply -f uaa.yaml