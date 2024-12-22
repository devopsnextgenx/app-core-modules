package io.devopsnextgenx.microservices.modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.devopsnextgenx.microservices.modules.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.modules.services.user-auth-service}")
    private String userAuthServiceUrl;
    
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    public UserDto getUser(String userId) {
        String url = userAuthServiceUrl + userId;
        return restTemplate.getForObject(url, UserDto.class);
    }

    public List<UserDto> listUsers() {
        String url = userAuthServiceUrl;
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {}).getBody();
    }

    public List<UserDto> listUsersLoadBalancer() {
        return restTemplate.exchange("https://user-auth-service/api/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {}).getBody();
    }
    
    public void putUserById(String userId, UserDto user) {
        String url = userAuthServiceUrl + userId;
        restTemplate.put(url, user);
    }

    public UserDto patchUserById(String userId, UserDto user) {
        String url = userAuthServiceUrl + userId;
        return restTemplate.patchForObject(url, user, UserDto.class);
    }

    public String postUser(String user) {
        String url = "http://user-auth-service/api/users";
        return restTemplate.postForObject(url, user, String.class);
    }
    
}
