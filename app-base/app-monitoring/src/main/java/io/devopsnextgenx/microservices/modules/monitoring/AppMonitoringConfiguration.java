package io.devopsnextgenx.microservices.modules.monitoring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.monitoring.enabled", havingValue = "true", matchIfMissing = false)
public class AppMonitoringConfiguration {
      
}
