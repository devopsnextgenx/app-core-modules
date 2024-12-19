# app-core-modules

### Start localstack and mysql server

```bash
cd localstack
docker compose up -d
```

### Setup Keystore

```bash
# For Linux, find cacerts and use that path
sudo keytool -importcert -alias appx -file appx-services/app-eureka-config-service/src/main/resources/appx.crt -keystore /etc/ssl/certs/java/cacerts -storepass changeit

# For Windows, find the JDK involved and use path accordingly
keytool -importcert -alias appx -file appx-services/app-eureka-config-service/src/main/resources/appx.crt -keystore "c:/Program Files/Zulu/zulu-17/lib/security/cacerts" -storepass changeit
```
### Package and Install

```bash
mvn clean install
# Skip tests
mvn clean install -DskipTests
```
### Start Services

```bash
# Start Registry/Config server
cd appx-services/app-eureka-config-service
mvn spring-boot:run

# Start app-user-auth-service
cd appx-services/app-user-auth-service
mvn spring-boot:run

# Start app-user-auth-service
cd appx-services/user-service
mvn spring-boot:run
```

### Import XML Data
- Open `Swagger UI` for [User Auth Service](https://user-auth-service.appx.localtest.me:2001/swagger-ui/index.html)
    - Use `user/password` or `config/password` as Basic Authentication to generate jwt token
    - Use generated token to set `JWT token` to invoke respective endpoints
    - Run `ImportXml` endpoint with `INCREMENTAL` or `OVERRIDE` to create default users

### Other generic instructions

- Open [Eureka Dashboard](https://eureka.appx.localtest.me:8761/) with `config/password`
- Open `Swagger UI` for [User Service](https://user-service.appx.localtest.me:8080/swagger-ui/index.html)
    - Use `user/password` or `config/password` as Basic Authentication to generate jwt token
    - Use generated token to set `JWT token` to invoke respective endpoints

- With `jwt-token-generator`, Use user `admin/admin` or `user/user` for generating jwt token using 
- With `demo-controller`, Use JWT token to access protected endpoints