../gradlew clean :example-service-organization:build
docker rmi -f organization:0.0.1
docker build -f Dockerfile -t organization:0.0.1 .
kubectl delete -f organization.yaml
kubectl apply -f organization.yaml