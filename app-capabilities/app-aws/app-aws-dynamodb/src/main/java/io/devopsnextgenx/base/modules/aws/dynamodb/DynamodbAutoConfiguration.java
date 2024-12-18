package io.devopsnextgenx.base.modules.aws.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.aws.dynamodb.integrative.DynamoDBIntegrativeInitializer;
import io.devopsnextgenx.base.modules.aws.dynamodb.local.DynamoDBLocalInitializer;
import io.devopsnextgenx.base.modules.aws.dynamodb.providers.AmazonDynamoDBClientProvider;
import io.devopsnextgenx.base.modules.aws.dynamodb.providers.AwsIntegrativeDynamoDBProvider;
import io.devopsnextgenx.base.modules.aws.dynamodb.providers.AwsLocalDynamoDBProvider;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * DynamodbAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class DynamodbAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "app.modules.dynamodb")
    public AppDynamoDBConfig appDynamoDBConfig() {
        return new AppDynamoDBConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    AmazonDynamoDBClientProvider amazonDynamoDBClientProvider(AppDynamoDBConfig config, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        if (config.isLocal()) {
            log.info("Initializing Local AmazonDynamoDB Client");
            return new AwsLocalDynamoDBProvider();
        } else {
            Objects.requireNonNull(config.getEnvironmentPrefix(), "'environmentPrefix' parameter must be specified in runtime mode.");
            log.info("Initializing Integrative AmazonDynamoDB Client");
            return new AwsIntegrativeDynamoDBProvider(credentials, awsProperties);
        }
    }

    @Bean
    AmazonDynamoDB amazonDynamoDB(AmazonDynamoDBClientProvider provider, AppDynamoDBConfig config) throws AppAwsResourceException {
        return provider.provide(config);
    }

    @Bean
    @ConditionalOnProperty(name = "app.modules.dynamodb.local", havingValue = "true")
    public BaseDynamoDBMapperInitializer dynamoDBLocalInitializer(AppDynamoDBConfig config, AmazonDynamoDB dynamoDB, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        return new DynamoDBLocalInitializer(config, dynamoDB, credentials, awsProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseDynamoDBMapperInitializer dynamoDBMapperIntegrativeInitializer(AppDynamoDBConfig config, AmazonDynamoDB dynamoDB, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        return new DynamoDBIntegrativeInitializer(config, dynamoDB, credentials, awsProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamoDBMapper dynamoDBMapper(BaseDynamoDBMapperInitializer baseDynamoDBMapperInitializer) {
        return baseDynamoDBMapperInitializer.dynamoDBMapper();
    }
}
