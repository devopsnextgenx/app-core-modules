# app-core-modules

### Start localstack and mysql server

```bash
cd localstack
docker compose up -d
```

### Test User-Service

```bash

mvn clean install

# Skip tests
mvn clean install -DskipTests

cd user-service
mvn spring-boot:run

```

### Browser

- Open url [swagger-ui](http://localhost:8080/swagger-ui/index.html)
- With `jwt-token-generator`, Use user `admin/admin` or `user/user` for generating jwt token using 
- With `demo-controller`, Use JWT token to access protected endpoints