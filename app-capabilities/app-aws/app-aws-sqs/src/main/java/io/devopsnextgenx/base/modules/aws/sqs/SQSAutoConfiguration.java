package io.devopsnextgenx.base.modules.aws.sqs;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import io.devopsnextgenx.base.modules.aws.sqs.integrative.AmazonSQSIntegrativeInitializer;
import io.devopsnextgenx.base.modules.aws.sqs.local.AmazonSQSLocalInitializer;
import io.devopsnextgenx.base.modules.aws.sqs.msg.QueueNameValidator;
import io.devopsnextgenx.base.modules.aws.sqs.msg.SqsMessageSender;
import io.devopsnextgenx.base.modules.aws.sqs.providers.AmazonSQSClientProvider;
import io.devopsnextgenx.base.modules.aws.sqs.providers.AwsLocalSQSClientProvider;
import io.devopsnextgenx.base.modules.aws.sqs.providers.AwsRuntimeSQSClientProvider;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.cloud.aws.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.cloud.aws.messaging.support.converter.NotificationRequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * SQSAutoConfiguration:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class SQSAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "appx.modules.sqs")
    public AppSqsConfig appSqsConfig() {
        return new AppSqsConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonSQSClientProvider amazonSQSClientProvider(AppSqsConfig config, AWSCredentialsProvider credentials, AppAwsProperties awsProperties) {
        if (config.isLocal()) {
            log.info("Initializing Local AmazonSQS Client");
            return new AwsLocalSQSClientProvider();
        } else {
            Objects.requireNonNull(config.getEnvironmentPrefix(), "'environmentPrefix' parameter must be specified in runtime mode.");
            log.info("Initializing Integrative AmazonSQS Client");
            return new AwsRuntimeSQSClientProvider(credentials, awsProperties);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public AmazonSQS amazonSQS(AmazonSQSClientProvider provider, AppSqsConfig config) throws AppAwsResourceException {
        return provider.provide(config);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AmazonSQSAsync amazonSQSAsync(AmazonSQSClientProvider provider, AppSqsConfig config) throws AppAwsResourceException {
        return provider.provideAsync(config);
    }

    @Bean
    public AmazonSQSIntegrativeInitializer amazonSQSInitializer(AppSqsConfig config, AmazonSQS amazonSqs) {
        return new AmazonSQSIntegrativeInitializer(config, amazonSqs).initialize();
    }

    @Bean
    @ConditionalOnProperty(name = "appx.modules.sqs.local", havingValue = "true")
    AmazonSQSLocalInitializer amazonSQSLocalInitializer(AppSqsConfig config, AmazonSQS amazonSqs) {
        return new AmazonSQSLocalInitializer(config, amazonSqs);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueueNameValidator queueNameValidator(AppSqsConfig config) {
        return new QueueNameValidator(config);
    }

    @Bean
    @ConditionalOnMissingBean
    SqsMessageSender sqsMessageSender(QueueMessagingTemplate queueMessagingTemplate, QueueNameValidator queueNameValidator) {
        return new SqsMessageSender(queueMessagingTemplate, queueNameValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
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
    public SimpleMessageListenerContainer simpleMessageListenerContainer(SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory, QueueMessageHandler queueMessageHandler){
        SimpleMessageListenerContainer simpleMessageListenerContainer = simpleMessageListenerContainerFactory.createSimpleMessageListenerContainer();
        simpleMessageListenerContainer.setMessageHandler(queueMessageHandler);
        return simpleMessageListenerContainer;
    }

    @Bean
    @ConditionalOnMissingBean
    public QueueMessageHandler queueMessageHandler(QueueMessageHandlerFactory queueMessageHandlerFactory) {
        QueueMessageHandler queueMessageHandler = queueMessageHandlerFactory.createQueueMessageHandler();
        return queueMessageHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter mappingJackson2MessageConverter =
                new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setSerializedPayloadClass(String.class);
        mappingJackson2MessageConverter.setObjectMapper(objectMapper);
        mappingJackson2MessageConverter.setStrictContentTypeMatch(true);
        return mappingJackson2MessageConverter;
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(
            AmazonSQSAsync amazonSQS, BeanFactory beanFactory, MappingJackson2MessageConverter mappingJackson2MessageConverter) {

        // Wrapped in this
        List<MessageConverter> payloadArgumentConverters = new ArrayList<>();
        payloadArgumentConverters.add(mappingJackson2MessageConverter);

        // This is the converter that is invoked on SNS messages on SQS listener
        NotificationRequestConverter notificationRequestConverter =
                new NotificationRequestConverter(mappingJackson2MessageConverter);

        payloadArgumentConverters.add(notificationRequestConverter);
        payloadArgumentConverters.add(new SimpleMessageConverter());

        CompositeMessageConverter compositeMessageConverter =
                new CompositeMessageConverter(payloadArgumentConverters);
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();

        factory.setAmazonSqs(amazonSQS);
        factory.setBeanFactory(beanFactory);
        factory.setArgumentResolvers(Arrays.asList(
                new NotificationMessageArgumentResolver(compositeMessageConverter)));
        return factory;
    }
}
