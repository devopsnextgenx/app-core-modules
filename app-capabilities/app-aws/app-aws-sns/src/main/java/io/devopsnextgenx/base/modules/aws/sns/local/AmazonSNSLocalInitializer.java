package io.devopsnextgenx.base.modules.aws.sns.local;

import com.amazonaws.services.sns.AmazonSNS;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AmazonSNSLocalInitializer {
    private AppSnsConfig config;
    private AmazonSNS amazonSns;

    @EventListener(ContextRefreshedEvent.class)
    public void createTablesForDev() {
        doCreation();
    }

    public void doCreation() {
        String envPrefix = Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env)
                .orElse(null);
        List<String> topicList = config.getTopicList();

        try {
            amazonSns.listTopics();
            topicList.stream().forEach(topicName-> {
                topicName = String.format("%s_%s", envPrefix, topicName);
                amazonSns.createTopic(topicName);
                log.info("Creating topic {}!!!", topicName);
            });
        } catch (Exception e) {
            log.error("Failed to list topics from local SNS, check if local SNS server is running:  {}", e.toString());
            throw new RuntimeException(String.format("Failed to list topics from local SNS, check if local SNS server is running:  %s", e.toString()));
        }
    }
}
