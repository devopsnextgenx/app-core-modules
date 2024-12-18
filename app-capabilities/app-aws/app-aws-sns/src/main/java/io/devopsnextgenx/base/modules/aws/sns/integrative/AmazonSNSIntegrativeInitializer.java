package io.devopsnextgenx.base.modules.aws.sns.integrative;

import com.amazonaws.services.sns.AmazonSNS;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AmazonSNSIntegrativeInitializer {
    private AppSnsConfig config;
    @SuppressWarnings("unused")
    private AmazonSNS amazonSns;

    public AmazonSNSIntegrativeInitializer initialize() {
        @SuppressWarnings("unused")
        String envPrefix = Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env + "_")
                .orElse(null);
        return this;
    }
}
