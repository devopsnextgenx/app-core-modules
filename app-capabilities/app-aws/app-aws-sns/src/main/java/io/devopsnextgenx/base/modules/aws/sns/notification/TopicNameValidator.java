package io.devopsnextgenx.base.modules.aws.sns.notification;

import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.Optional;

@AllArgsConstructor
public class TopicNameValidator implements TopicName {
    private AppSnsConfig config;

    /**
     * @param topicName Topic Name Enum that we would like to send msgs to,
     * @return a fully qualified name
     */
    public String validateAndAddPrefixToTopicName(String topicName) {
        String envPrefix = getEnvironmentPrefix();
        Assert.notNull(envPrefix, "Env Prefix Must Be Defined, either by the system or Env Properties as (-D)topic_ENV_PREFIX ");
        validateTopicName(topicName);
        return Optional.ofNullable(topicName)
                .map(s -> String.format("%s_%s", envPrefix, topicName))
                .orElseGet(null);
    }

    public void validateTopicName(String topicName) {
        config.getTopicList().stream()
                .filter(q-> q.equals(topicName))
                .findAny()
                .orElseThrow( ()-> new RuntimeException(String.format("Topic %s Requested is missing in the TopicNames List supplied by the bean", topicName)));
    }


    private String getEnvironmentPrefix() {
        return Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env)
                .orElse(null);
    }

    @Override
    public String getTopicName(String topicName) {
        return validateAndAddPrefixToTopicName(topicName);
    }
}
