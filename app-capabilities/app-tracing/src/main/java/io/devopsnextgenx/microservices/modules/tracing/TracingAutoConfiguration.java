package io.devopsnextgenx.microservices.modules.tracing;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.metrics.NoopMetricsFactory;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.jaegertracing.spi.Sampler;
import io.jaegertracing.thrift.internal.senders.UdpSender;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Configuration
@AutoConfiguration
@EnableConfigurationProperties
@ConditionalOnProperty(name = "appx.modules.tracing.enabled", havingValue = "true")
public class TracingAutoConfiguration {
    @Value("${spring.application.name}")
    private String SERVICE_NAME;
    @Value("${appx.modules.tracing.jaeger.host:localhost}")
    private String JAEGER_HOST;
    @Value("${appx.modules.tracing.jaeger.port:6831}")
    private int JAEGER_PORT;
    @Value("${appx.modules.tracing.jaeger.trace.samplingRate:1}")
    private double SAMPLING_RATE;
    @Value("${appx.modules.tracing.jaeger.trace.flushInterval:1000}")
    private int FLUSH_INTERVAL;
    @Value("${appx.modules.tracing.jaeger.trace.maxQueueSize:100}")
    private int MAX_QUEUE_SIZE;

    @Bean
    public Sampler sampler() {
        return new ProbabilisticSampler(SAMPLING_RATE);
    }

    @Bean("tracer")
    @ConditionalOnProperty(name = "appx.modules.tracing.jaeger.enabled", havingValue = "true")
    public Tracer tracer(RemoteReporter remoteReporter, Sampler sampler) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostName();
        return new JaegerTracer.Builder(SERVICE_NAME)
                .withTag("ServiceName", SERVICE_NAME)
                .withTag("HostName", hostAddress)
                .withReporter(remoteReporter)
                .withMetricsFactory(new NoopMetricsFactory())
                .withSampler(sampler)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "appx.modules.tracing.jaeger.enabled", havingValue = "true")
    public RemoteReporter remoteReporter() {
        return new RemoteReporter.Builder()
                .withFlushInterval(FLUSH_INTERVAL)
                .withMaxQueueSize(MAX_QUEUE_SIZE)
                .withSender(new UdpSender(JAEGER_HOST, JAEGER_PORT, 0)).build();
    }
}
