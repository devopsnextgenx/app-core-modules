### Build Docker image:
```
docker build -f localstack-ui.Dockerfile . -t amitkshirsagar13/localstack-ui
docker-compose up --remove-orphans


```


### Vault Setup
```
docker exec -it vault sh

export VAULT_ADDR="http://127.0.0.1:8200"
export export VAULT_TOKEN=00000000-0000-0000-0000-000000000000
vault kv put secret/application test.username=defaultuser test.password=defaultpassword
vault kv put secret/test-vault-config test.password=testpassword
vault kv put secret/test-vault-config/cloud test.username=clouduser test.password=cloudpassword

```

### Strtup Docker-Compose

```
docker-compose down --remove-orphan && docker-compose up -d
```