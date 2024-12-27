package io.devopsnextgenx.microservices.modules.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName config-eureka
 * Creation date: Sep 28, 2017
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */
@EnableEurekaServer
@EnableConfigServer
@SpringBootApplication
@ComponentScan(basePackages = "io.devopsnextgenx.microservices.modules")
@EnableJpaRepositories("io.devopsnextgenx.microservices.modules.security")
public class EurekaConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaConfigServerApplication.class, args);
	}
}
