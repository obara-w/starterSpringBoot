# Customer Service

```bash
cd customer
```

## Run postgres on Docker

```bash
docker run \
  --name=postgres-springboot \
  --rm \
  --network=springboot \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_USER=customer \
  postgres
```

## Build jar

```bash
./mvnw install \
  -Dspring.profiles.active=test \
  -DPOSTGRES_DB_USER=customer \
  -DPOSTGRES_DB_HOST=localhost \
  -DPOSTGRES_DB_NAME=customer \
  -DPOSTGRES_DB_PASS=password
```

## Build Image

```bash
docker build -t customer .
```

## Run Image on docker

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
  -e FRAUD_ENDPOINT="http://fraud:8080/api/v1/fraud-check" \
  customer
```

## Push Image to Azure Container Registry

### login 

```bash
az login
az acr login --name starterregistry
```

### Tag the image

```bash
docker tag customer starterregistry.azurecr.io/starter/customer:1.0.0
```

### Push the image

```bash
docker push starterregistry.azurecr.io/starter/customer:1.0.0
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
  -e FRAUD_ENDPOINT="http://fraud:8080/api/v1/fraud-check" \
  starterregistry.azurecr.io/starter/customer:1.0.0
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
cd k8s/services/customer/
kubectl apply -f deployment.yml
kubectl apply -f service.yml
```

```bash
kubectl logs -f -c customer customer-6dd8fdf5ff-6rhd2
```

### Restart Deployment

```bash
kubectl rollout restart deployment customer
```

```bash

```