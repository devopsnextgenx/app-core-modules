package io.devopsnextgenx.x;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.PropertySource;

import io.devopsnextgenx.base.modules.config.YamlPropertyLoaderFactory;
import io.devopsnextgenx.microservices.modules.logging.config.LoggingAutoConfiguration;
import io.devopsnextgenx.microservices.modules.tracing.TracingAutoConfiguration;

@SpringBootTest(classes = { LoggingAutoConfiguration.class, TracingAutoConfiguration.class })
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoaderFactory.class)
class XApplicationTests {
	@Autowired
	FilterRegistrationBean tomcatApiLoggerFilterRegistrationBean;

    @Test
    void testSamplerBeanInitialized() {
        tomcatApiLoggerFilterRegistrationBean.getFilter();
    }


}
