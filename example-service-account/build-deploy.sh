../gradlew clean :example-service-account:build
docker rmi -f account:0.0.1
docker build -f Dockerfile -t account:0.0.1 .
kubectl delete -f account.yaml
kubectl apply -f account.yaml