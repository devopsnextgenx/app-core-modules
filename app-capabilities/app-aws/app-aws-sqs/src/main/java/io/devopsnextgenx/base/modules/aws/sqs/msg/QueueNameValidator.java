package io.devopsnextgenx.base.modules.aws.sqs.msg;

import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.Optional;

@AllArgsConstructor
public class QueueNameValidator implements QueueName {
    private AppSqsConfig config;

    /**
     * @param queueName Queue Name Enum that we would like to send msgs to,
     * @return a fully qualified name
     */
    public String validateAndAddPrefixToQueueName(String queueName) {
        String envPrefix = getEnvironmentPrefix();
        Assert.notNull(envPrefix, "Env Prefix Must Be Defined, either by the system or Env Properties as (-D)QUEUE_ENV_PREFIX ");
        validateQueueName(queueName);
        return Optional.ofNullable(queueName)
                .map(s -> String.format("%s_%s", envPrefix, queueName))
                .orElseGet(null);
    }

    public void validateQueueName(String queueName) {
        config.getQueueList().stream()
                .filter(q-> q.equals(queueName))
                .findAny()
                .orElseThrow( ()-> new RuntimeException(String.format("QueueName [%s] Requested is missing in the QueueNames List supplied by the bean", queueName)));
    }


    private String getEnvironmentPrefix() {
        return Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env)
                .orElse(null);
    }

    @Override
    public String getQueueName(String queueName) {
        return validateAndAddPrefixToQueueName(queueName);
    }
}
