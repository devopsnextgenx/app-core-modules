package io.devopsnextgenx.microservices.modules.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.monitoring.enabled", havingValue = "true", matchIfMissing = false)
public class AppMonitoringConfiguration {
      
}
