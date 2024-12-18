package io.devopsnextgenx.base.modules.aws.sqs.integrative;

import com.amazonaws.services.sqs.AmazonSQS;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AmazonSQSIntegrativeInitializer {
    private AppSqsConfig config;
    @SuppressWarnings("unused")
    private AmazonSQS amazonSqs;

    public AmazonSQSIntegrativeInitializer initialize() {
        @SuppressWarnings("unused")
        String envPrefix = Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env + "_")
                .orElse(null);
        return this;
    }
}
