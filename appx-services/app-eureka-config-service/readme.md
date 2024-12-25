### Setup Certificate
```bash
# Create Certificate
# Password: appxxppa
keytool -genkeypair -alias appx \
  -keyalg RSA -keysize 2048 \
  -storetype PKCS12 \
  -keystore appx-services/app-eureka-config-service/src/main/resources/appx.p12 \
  -validity 3650 \
  -dname "CN=*.appx.localtest.me,OU=appx,O=devopsnextgenx,L=Troy,S=MI,C=US" \
  -ext "san=dns:*.appx.localtest.me"

# Export the certificate
keytool -exportcert -alias appx -keystore appx-services/app-eureka-config-service/src/main/resources/appx.p12 -storetype PKCS12 -file src/main/resources/appx.crt
sudo keytool -delete -alias appx -keystore /etc/ssl/certs/java/cacerts -storepass changeit
sudo keytool -importcert -alias appx -file appx-services/app-eureka-config-service/src/main/resources/appx.crt -keystore /etc/ssl/certs/java/cacerts -storepass changeit

# Import the certificate -> remove existing alias present and then import new certificate with same alias

keytool -delete -alias appx -keystore "c:/Program Files/Zulu/zulu-17/lib/security/cacerts" -storepass changeit
keytool -importcert -alias appx -file appx-services/app-eureka-config-service/src/main/resources/appx.crt -keystore "c:/Program Files/Zulu/zulu-17/lib/security/cacerts" -storepass changeit


keytool -importkeystore  -srckeystore appx-services/app-eureka-config-service/src/main/resources/appx.p12 -destkeystore appx-services/app-eureka-config-service/src/main/resources/appx.jks -srcstoretype PKCS12 -deststoretype jks -srcstorepass appxxppa -deststorepass appxxppa -srcalias appx -destalias appx -srckeypass appxxppa -destkeypass appxxppa

keytool -importkeystore -srckeystore appx-services/app-eureka-config-service/src/main/resources/appx.p12 -destkeystore appx-services/app-eureka-config-service/src/main/resources/appx.jks -srcstoretype PKCS12 -deststoretype jks -srcstorepass appxxppa -deststorepass appxxppa
```

### Actuator Urls:

- [info](https://eureka-service.appx.localtest.me:8761/management/actuator/info)
- [health](https://eureka-service.appx.localtest.me:8761/management/actuator/health)

### Eureka Server:
- [eureka-service](https://eureka-service.appx.localtest.me:8761/)

### Config Server:

- [user-service:default](https://config-service.appx.localtest.me:8761/config/user-service/default)
- [user-service:local](https://config-service.appx.localtest.me:8761/config/user-service/local)
- [user-service:dev](https://config-service.appx.localtest.me:8761/config/user-service/dev)