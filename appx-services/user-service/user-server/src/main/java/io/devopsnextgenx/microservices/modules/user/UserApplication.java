package io.devopsnextgenx.microservices.modules.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
// @SpringBootApplication(exclude = {
// 	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
// })
@ComponentScan(basePackages = "io.devopsnextgenx")
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
