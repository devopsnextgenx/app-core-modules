package io.devopsnextgenx.microservices.modules.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import io.devopsnextgenx.base.modules.config.YamlPropertyLoaderFactory;
import io.jaegertracing.spi.Sampler;
import io.opentracing.Tracer;

import org.junit.jupiter.api.Test;


@SpringBootTest(classes = {TracingAutoConfiguration.class})
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoaderFactory.class)
class TracingAutoConfigurationTest {

    @Autowired
    private Sampler sampler;

    @Autowired
    Tracer tracer;

    @Test
    void testSamplerBeanInitialized() {
        sampler.sample("null", 0);
    }
    
    @Test
    void testTracerBeanInitialized() {
        tracer.buildSpan("Hello World");
    }
}