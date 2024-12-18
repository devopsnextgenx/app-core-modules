package io.devopsnextgenx.base.modules.config.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerModuleAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class AwsEnvironmentModuleAutoConfiguration {

    @Bean
    @ConfigurationProperties("app.aws")
    @ConditionalOnMissingBean
    public AppAwsProperties appAwsProperties(){
        return new AppAwsProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public AWSCredentialsProvider awsCredentialsProvider(AppAwsProperties appAwsProperties){
        DefaultAWSCredentialsProviderChain instance = DefaultAWSCredentialsProviderChain.getInstance();
        log.info("AWS Credentials class is: '{}'", instance.getClass().getSimpleName());
        return instance;
    }

}
