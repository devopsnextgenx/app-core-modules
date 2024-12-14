### Setup Certificate
```bash
# Create Certificate

keytool -genkeypair -alias k8s -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore k8s.p12 -validity 3650

keytool -keyalg RSA -genkey -alias eureka -keystore eureka.jks
keytool -keyalg RSA -genkey -alias client -keystore client.jks

# Export certificate
keytool -export -alias client -file client.crt -keystore client.jks
keytool -export -alias eureka -file eureka.crt -keystore eureka.jks

# Import certificate
keytool -import -alias eureka -file eureka.p12 -keystore client.jks
keytool -import -alias client -file client.crt -keystore eureka.jks



keytool -exportcert -keystore eureka-config-service/src/main/resources/k8s.p12 -storepass 123456 -storetype PKCS12 -alias k8s -file eureka-config-service/src/main/resources/k8s.cer
keytool -importcert -keystore /etc/ssl/certs/java/cacerts -storepass changeit -alias k8s -file eureka-config-service/src/main/resources/k8s.cer
```

### Actuator Urls:

- [info](https://eureka.k8s.localtest.me:8761/management/actuator/info)
- [health](https://eureka.k8s.localtest.me:8761/management/actuator/health)

### Config Server:

- [user-service:default](https://eureka.k8s.localtest.me:8761/config/user-service/default)
- [user-service:local](https://eureka.k8s.localtest.me:8761/config/user-service/local)
- [user-service:dev](https://eureka.k8s.localtest.me:8761/config/user-service/dev)