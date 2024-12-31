package io.devopsnextgenx.microservices.modules.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.devopsnextgenx.microservices.modules.product.mappers.ProductMapper;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "io.devopsnextgenx")
@EnableMongoRepositories("io.devopsnextgenx.microservices.modules.product.repository")
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Bean
	public ProductMapper productMapper() {
		return new ProductMapper();
	}
}
