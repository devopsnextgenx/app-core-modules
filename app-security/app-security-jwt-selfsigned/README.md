# Security JWT Self-Signed Validator

Injects a [Self-Signed TokenValidator](src/main/java/io/devopsnextgenx/microservices/modules/security/jwt/SelfSignedTokenValidator.java) 
that is validating a fake self-signed token. This allows us to bypass Auth0 in "test" profiles and also to easily create 
"authenticated" users without the need to really create them in external systems.

>  This plugin should not be bundled in any server artifact that goes to production.

The plugin is authenticating JWT that were signed using `OAuthLoginHelper::loginSelfSigned` method.
Both are using same configuration to sign and verify.

## Configurations & Operation
This plugin is looking for configurations under `app.oauth.applications.oauth-selfsigned` with the 
following set of properties:

```yaml
app:
  oauth.applications:
    oauth-selfsigned:
          domain: https://devopsnextgenx.localtest.me/
          dBConnection: SelfSigned-Authentication
          audience: SelfSigned-Client-ID
    oauth-v2-selfsigned:
            domain: https://devopsnextgenx.localtest.me/
            dBConnection: SelfSigned-Authentication-V2
            audience: SelfSigned-Client-ID-V2
            expirationSec: 808080
            notBeforeSec: 4
            issuedAtSec: 4
```

## IntelliJ Adoption
There are several steps that are necessary to follow when starting services from IntelliJ

### Spring Boot MicroServices
MicroServices has the ability to validate both Auth0(production) token and Self-Signed tokens (test only).
If "Include dependecies with provided scope" is checked :
 ![atltext Add batch to run group](./docs/include-provided-artifacts.png)
   
The `security-jwt-selfsigned` artifact will be included in Spring classpath and the `SelfSignedTokenValidator` will
take place instead of the `ProductionTokenValidator` default one.


