### Setup Certificate
```bash
# Create Certificate
# Password: appxxppa
keytool -genkeypair -alias appx \
  -keyalg RSA -keysize 2048 \
  -storetype PKCS12 \
  -keystore src/main/resources/appx.p12 \
  -validity 3650 \
  -dname "CN=*.appx.localtest.me,OU=appx,O=devopsnextgenx,L=Troy,S=MI,C=US" \
  -ext "san=dns:*.appx.localtest.me"

# Export the certificate
keytool -exportcert -alias appx -keystore src/main/resources/appx.p12 -storetype PKCS12 -file src/main/resources/appx.crt

# Import the certificate -> remove existing alias present and then import new certificate with same alias
sudo keytool -delete -alias appx -keystore /etc/ssl/certs/java/cacerts -storepass changeit
sudo keytool -importcert -alias appx -file src/main/resources/appx.crt -keystore /etc/ssl/certs/java/cacerts -storepass changeit

keytool -importcert -alias appx -file src/main/resources/appx.crt -keystore "c:/Program Files/Zulu/zulu-17/lib/security/cacerts" -storepass changeit
```

### Actuator Urls:

- [info](https://eureka.appx.localtest.me:8761/management/actuator/info)
- [health](https://eureka.appx.localtest.me:8761/management/actuator/health)

### Config Server:

- [user-service:default](https://eureka.appx.localtest.me:8761/config/user-service/default)
- [user-service:local](https://eureka.appx.localtest.me:8761/config/user-service/local)
- [user-service:dev](https://eureka.appx.localtest.me:8761/config/user-service/dev)