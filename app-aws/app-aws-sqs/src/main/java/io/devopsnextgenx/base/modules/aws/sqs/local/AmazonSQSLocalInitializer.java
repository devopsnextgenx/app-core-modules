package io.devopsnextgenx.base.modules.aws.sqs.local;

import com.amazonaws.services.sqs.AmazonSQS;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AmazonSQSLocalInitializer {
    private AppSqsConfig config;
    private AmazonSQS amazonSqs;

    @EventListener(ContextRefreshedEvent.class)
    public void createTablesForDev(ContextRefreshedEvent event) {
        doCreation();
    }

    public void doCreation() {
        String envPrefix = Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env)
                .orElse(null);
        List<String> queueList = config.getQueueList();

        try {
            queueList.stream().forEach(queueName-> {
                queueName = String.format("%s_%s", envPrefix, queueName);
                amazonSqs.createQueue(queueName);
                log.info("Creating queue {}!!!", queueName);
            });
            queueList.stream().forEach(queueName-> {
                queueName = String.format("%s_%s", envPrefix, queueName);
                if (amazonSqs.getQueueUrl(queueName) != null) {
                    log.info("Queue {} exists!!!", queueName);
                }
            });
        } catch (Exception e) {
            log.error("Failed to list queues from local SQS, check if local SQS server is running:  {}", e.toString());
            throw new RuntimeException(String.format("Failed to list queues from local SQS, check if local SQS server is running: %s", e.toString()));
        }
    }
}
