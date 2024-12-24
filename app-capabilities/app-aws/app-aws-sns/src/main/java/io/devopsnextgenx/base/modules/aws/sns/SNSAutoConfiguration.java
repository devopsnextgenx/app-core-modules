package io.devopsnextgenx.base.modules.aws.sns;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
import io.devopsnextgenx.base.modules.aws.sns.integrative.AmazonSNSIntegrativeInitializer;
import io.devopsnextgenx.base.modules.aws.sns.local.AmazonSNSLocalInitializer;
import io.devopsnextgenx.base.modules.aws.sns.notification.SnsNotifictionSender;
import io.devopsnextgenx.base.modules.aws.sns.notification.TopicNameValidator;
import io.devopsnextgenx.base.modules.aws.sns.providers.AmazonSNSClientProvider;
import io.devopsnextgenx.base.modules.aws.sns.providers.AwsLocalSNSClientProvider;
import io.devopsnextgenx.base.modules.aws.sns.providers.AwsRuntimeSNSClientProvider;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * SNSAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class SNSAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "appx.modules.sns")
    public AppSnsConfig appSnsConfig() {
        return new AppSnsConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQS){
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSQS);
        factory.setMaxNumberOfMessages(10);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonSNSClientProvider amazonSNSClientProvider(AppSnsConfig config, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        if (config.isLocal()) {
            log.info("Initializing Local AmazonSNS Client");
            return new AwsLocalSNSClientProvider();
        } else {
            Objects.requireNonNull(config.getEnvironmentPrefix(), "'environmentPrefix' parameter must be specified in runtime mode.");
            log.info("Initializing Integrative AmazonSNS Client");
            return new AwsRuntimeSNSClientProvider(credentials, awsProperties);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonSNS amazonSNS(AmazonSNSClientProvider provider, AppSnsConfig config) throws AppAwsResourceException {
        return provider.provide(config);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AmazonSNSAsync amazonSNSAsync(AmazonSNSClientProvider provider, AppSnsConfig config) throws AppAwsResourceException {
        return provider.provideAsync(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonSNSIntegrativeInitializer amazonSNSInitializer(AppSnsConfig config, AmazonSNS amazonSNS) {
        return new AmazonSNSIntegrativeInitializer(config, amazonSNS).initialize();
    }

    @Bean
    @ConditionalOnProperty(name = "appx.modules.sns.local", havingValue = "true")
    AmazonSNSLocalInitializer amazonSNSLocalInitializer(AppSnsConfig config, AmazonSNS amazonSNS) {
        return new AmazonSNSLocalInitializer(config, amazonSNS);
    }

    @Bean
    @ConditionalOnMissingBean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        return new NotificationMessagingTemplate(amazonSNS);
    }

    @Bean
    @ConditionalOnMissingBean
    public TopicNameValidator topicNameValidator(AppSnsConfig config) {
        return new TopicNameValidator(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public SnsNotifictionSender snsNotifictionSender(NotificationMessagingTemplate notificationMessagingTemplate, TopicNameValidator topicNameValidator) {
        return new SnsNotifictionSender(notificationMessagingTemplate, topicNameValidator);
    }
}
