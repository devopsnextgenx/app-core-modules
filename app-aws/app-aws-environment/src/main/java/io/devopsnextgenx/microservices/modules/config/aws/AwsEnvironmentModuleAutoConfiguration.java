package io.devopsnextgenx.microservices.modules.config.aws;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.util.Optional;

@Slf4j
@Configuration
@EnableConfigurationProperties
public class AwsEnvironmentModuleAutoConfiguration {
    @Bean
    @ConfigurationProperties("app.aws")
    public AwsProperties appAwsProperties() {
        return new AwsProperties();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(AwsProperties awsProperties) {
        String credentialsClassName = Optional.ofNullable(awsProperties.getCredentialsClass())
                .orElseThrow(() -> new RuntimeException("Credentials class must be provided"));
        try {
            Class<?> credentialsClass = Class.forName(credentialsClassName);
            log.info("AWS Credentials class is: '{}'", credentialsClass.getSimpleName());
            return (AwsCredentialsProvider) credentialsClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Could not initialize AWS credentials, provided class unknown: '%s'", credentialsClassName));
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(String.format("Could not initialize AWS credentials, provided class  '%s' could not be initialized", credentialsClassName));
        }
    }
}
