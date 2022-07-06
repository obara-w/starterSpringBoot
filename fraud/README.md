# Fraud Service

```bash
cd fraud
```

## Build jar

```bash
./mvnw install \
  -Dspring.profiles.active=test 
```

## Build Image

```bash
docker build -t fraud .
```

## Run Image on docker

```bash
docker run \
  --name=fraud \
  --rm \
  --network=springboot \
  -p 8081:8080 \
  -e COSMOS_ENDPOINT="https://cosmo-core.documents.azure.com:443" \
  -e COSMOS_KEY='8Dx3Zihm4mQe4A5ooaklvG1o44GW8xlqpGyx3oAwfksHSaztSG1jaW0VOivv1bczufQ2zVxCpK5QFEOuH672Gg==' \
  -e COSMOS_DATABASE=myDatabase \
  fraud
```

## Push Image to Azure Container Registry

### login 

```bash
az login
az acr login --name starterregistry
```

### Tag the image

```bash
docker tag fraud starterregistry.azurecr.io/starter/fraud:1.0.0
```

### Push the image

```bash
docker push starterregistry.azurecr.io/starter/fraud:1.0.0
```

### Run pushed Image on docker

```bash
docker run \
  --name=customer \
  --rm \
  --network=springboot \
  -p 8080:8080 \
  -e POSTGRES_DB_USER=customer \
  -e POSTGRES_DB_HOST=postgres-springboot \
  -e POSTGRES_DB_NAME=customer \
  -e POSTGRES_DB_PASS=password \
  starterregistry.azurecr.io/starter/fraud:1.0.0
```

## Deploy To Kubernetes

### Configuring Container Registry integration for the AKS cluster

```bash
# check
az role assignment list \
--scope /subscriptions/fd8128a7-5818-4110-8c0f-03f8af3fed0e/resourceGroups/k8s/providers/Microsoft.ContainerRegistry/registries/starterregistry \
-o table

az aks update -n my-k8s-cluster -g k8s --attach-acr starterregistry
```

### Connect to Azure Kubernetes
```bash
az account set --subscription fd8128a7-5818-4110-8c0f-03f8af3fed0e
az aks get-credentials --resource-group k8s --name my-k8s-cluster
```

### Deploying the application

```bash
cd k8s/services/fraud/
kubectl apply -f deployment.yml
kubectl apply -f service.yml
```

```bash
kubectl get pods
kubectl get services
kubectl logs -f -c fraud fraud-5c5648d4c5-2zwxt
```

### Restart Deployment

```bash
kubectl rollout restart deployment fraud
```

```bash

```