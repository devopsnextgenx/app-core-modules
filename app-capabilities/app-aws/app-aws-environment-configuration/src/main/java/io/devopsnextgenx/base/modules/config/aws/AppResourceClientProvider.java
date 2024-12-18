package io.devopsnextgenx.base.modules.config.aws;

import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class AppResourceClientProvider {

    public AppResource getResourceDetails(AppAwsResourceProperties config) throws AppAwsResourceException {
        int httpPort = Optional.ofNullable(config.getDevelopment())
                .map(AppAwsResourceProperties.Development::getHttpPort)
                .orElseThrow(() -> new AppAwsResourceException("Define Http Port"));
        String hostname = Optional.ofNullable(config.getDevelopment())
                .map(AppAwsResourceProperties.Development::getHostName)
                .orElseThrow(() -> new AppAwsResourceException("Define HostName"));
        return new AppResource(httpPort,hostname);
    }

    @Data
    @AllArgsConstructor
    public class AppResource {
        private int httpPort;
        private String hostname;
    }
}
