# Getting Started

Application Runs on default port as 8080

Can be configured to use any port on container by defining the VPORT variable.



## Reference Documentation
### Actuator Urls:

- [info](https://user-service.k8s.localtest.me:8080/management/actuator/info)
- [health](https://user-service.k8s.localtest.me:8080/management/actuator/health)


### Echo Url:
Sample endpoint for echo: https://user-service.k8s.localtest.me:8080/api/echo

Get User by Id: https://user-service.appx.localtest.me:8080/api/user/123e4567-e89b-42d3-a456-556642440000


### Swagger Url:
Swagger endpoint : https://user-service.appx.localtest.me:8080/swagger-ui/index.html

### Actuator Endpoints:
- Prometheus: https://user-service.appx.localtest.me:8080/actuator/prometheus
- Health: https://user-service.appx.localtest.me:8080/actuator/health
- Info: https://user-service.appx.localtest.me:8080/actuator/info
- Metrics: https://user-service.appx.localtest.me:8080/actuator/metrics