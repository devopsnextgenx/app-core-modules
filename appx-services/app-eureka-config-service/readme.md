### Setup Certificate
```bash
# Create Certificate

keytool -genkeypair -alias appx -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore app-eureka-config-service/src/main/resources/appx.p12 -validity 3650
# Password: appxxppa
keytool -keyalg RSA -genkey -alias eureka -keystore eureka.jks
keytool -keyalg RSA -genkey -alias client -keystore client.jks

# Export certificate
keytool -export -alias client -file client.crt -keystore client.jks
keytool -export -alias eureka -file eureka.crt -keystore eureka.jks

# Import certificate
keytool -import -alias eureka -file eureka.p12 -keystore client.jks
keytool -import -alias client -file client.crt -keystore eureka.jks



keytool -exportcert -keystore app-eureka-config-service/src/main/resources/appx.p12 -storepass appxxppa -storetype PKCS12 -alias appx -file app-eureka-config-service/src/main/resources/appx.cer
keytool -importcert -keystore /etc/ssl/certs/java/cacerts -storepass changeit -alias appx -file app-eureka-config-service/src/main/resources/appx.cer
```

### Actuator Urls:

- [info](https://eureka.appx.localtest.me:8761/management/actuator/info)
- [health](https://eureka.appx.localtest.me:8761/management/actuator/health)

### Config Server:

- [user-service:default](https://eureka.appx.localtest.me:8761/config/user-service/default)
- [user-service:local](https://eureka.appx.localtest.me:8761/config/user-service/local)
- [user-service:dev](https://eureka.appx.localtest.me:8761/config/user-service/dev)