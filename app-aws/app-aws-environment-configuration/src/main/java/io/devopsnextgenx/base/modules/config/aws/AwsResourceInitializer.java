package io.devopsnextgenx.base.modules.config.aws;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface AwsResourceInitializer {
    @EventListener(ContextRefreshedEvent.class)
    void initializeResources();
}
