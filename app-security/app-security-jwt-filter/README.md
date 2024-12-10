# Security JWT Filter

This module is responsible for configuring and adding a Spring Boot 
[OncePerRequestFilter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/OncePerRequestFilter.html) 
to the security filter chain.

The actual JWT validation is abstracted by a [TokenValidator](/src/main/java/io/devopsnextgenx/microservices/modules/security/jwt/TokenValidator.java)
that validates JWT tokens against a [OIDC Complaint](https://openid.net/connect/) or [OAuth 2.0](https://oauth.net/2/) ID provider. 

The currently supported IDPs are:
* [Auth0](https://auth0.com)
* SelfSigned : bypass external IDP with an internal Self-Signed tokens for testing purpose

**Module operates on a single IDP configuration only !**

## Access Data & Authenticated Principal
As part of the token validation the security filter is also responsible to extract `access-data` header information and
eventually a  principal of a type `AccessDataAuthenticationToken` is set on the `SecurityContext`.  

The principal holds information about:
* User Email & ID
* User roles
* What company & organization he belongs to 
* Is that Org federated

> Note that currently we are not using the claims part of the JWT as part of the `Grants`

## Module Configuration

Main configuration data model is `OAuthApplicationsConfig` that holds the full list of known security tenants. The
provided configuration must follow this set of rules:

* Configuration is YAML that must be provided under `app.oauth` root element.
* The default IDP type is configure by `app.oauth.defaultAuthType` by the
  enum  [AuthProviderType](src/main/java/io/devopsnextgenx/microservices/modules/security/jwt/config/AuthProviderType.java)
* Other tenants should be provided under `app.oauth.applications`
* At least 2 tenants are mandatory: `auth0`, `auth0-v2`
* Each tenant must contain `clientSecret`, `clientId` and `domain`

### Example configuration

```yaml
app:
  oauth:
    defaultAuthType: AUTH0
    applications:
      auth0:
        domain: https://devopsnextgenx.auth0.com
        clientId: test-client-id
        clientSecret: fake-client-secret
        expirationSec: 808800
        notBeforeSec: 2
        issuedAtSec: 90
        authType: AUTH0
      self-signed:
        domain: ${AUTH_DOMAIN:https://devopsnextgenx.auth0.com/}
        clientId: ${AUTH_API_V2_clientId:https://devopsnextgenx.auth0.com/api/v2/}
        clientSecret: fake-client-secret
        authType: SELFSIGNED
```






