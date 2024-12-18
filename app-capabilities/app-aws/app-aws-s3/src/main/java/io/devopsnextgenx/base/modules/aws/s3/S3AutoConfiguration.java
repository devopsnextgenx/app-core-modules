package io.devopsnextgenx.base.modules.aws.s3;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import io.devopsnextgenx.base.modules.aws.s3.config.AppS3Config;
import io.devopsnextgenx.base.modules.aws.s3.integrative.AmazonS3IntegrativeInitializer;
import io.devopsnextgenx.base.modules.aws.s3.local.AmazonS3LocalInitializer;
import io.devopsnextgenx.base.modules.aws.s3.providers.AmazonS3ClientProvider;
import io.devopsnextgenx.base.modules.aws.s3.providers.AwsLocalS3ClientProvider;
import io.devopsnextgenx.base.modules.aws.s3.providers.AwsRuntimeS3ClientProvider;
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
 * S3AutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class S3AutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "app.modules.s3")
    public AppS3Config appS3Config() {
        return new AppS3Config();
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonS3ClientProvider amazonS3ClientProvider(AppS3Config config, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        if (config.isLocal()) {
            log.info("Initializing Local AmazonS3 Client");
            return new AwsLocalS3ClientProvider();
        } else {
            Objects.requireNonNull(config.getEnvironmentPrefix(), "'environmentPrefix' parameter must be specified in runtime mode.");
            log.info("Initializing Integrative AmazonS3 Client");
            return new AwsRuntimeS3ClientProvider(credentials, awsProperties);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 amazonS3(AmazonS3ClientProvider provider, AppS3Config config) throws AppAwsResourceException {
        return provider.provide(config);
    }

    @Bean
    public AmazonS3IntegrativeInitializer amazonS3Initializer(AppS3Config config, AmazonS3 amazonS3) {
        return new AmazonS3IntegrativeInitializer(config, amazonS3);
    }

    @Bean
    @ConditionalOnProperty(name = "app.modules.s3.local", havingValue = "true")
    AmazonS3LocalInitializer amazonS3LocalInitializer(AppS3Config config, AmazonS3 amazonS3) {
        return new AmazonS3LocalInitializer(config, amazonS3);
    }
}
