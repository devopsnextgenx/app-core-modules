package io.k8s.framework.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class ConfigClientBootstrapConfiguration implements BootstrapRegistryInitializer {

	@Value("${application.ssl.trustStore}")
	private String trustStore;

	@Value("${application.ssl.trustStorePassword}")
	private String trustStorePassword;

	@Override
	public void initialize(BootstrapRegistry registry) {
		final char[] password = trustStorePassword.toCharArray();
		final ClassPathResource resource = new ClassPathResource(trustStore);
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
					.loadKeyMaterial(resource.getFile(), password, password)
					.loadTrustMaterial(resource.getFile(), password, new TrustAllStrategy()).build();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpClient httpclient = HttpClients.custom()
        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext)
                        .build())
                .build())
        .build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);

		registry.register(RestTemplate.class, context -> {
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			return restTemplate;
		});
	}

	@Autowired
	ConfigClientProperties properties;

	@Bean("springSslContext")
	public SSLContext sslContext() {
		final char[] password = trustStorePassword.toCharArray();
		final ClassPathResource resource = new ClassPathResource(trustStore);
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
					.loadKeyMaterial(resource.getFile(), password, password)
					.loadTrustMaterial(resource.getFile(), password, new TrustAllStrategy()).build();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sslContext;
	}
	
	@Bean("closeableHttpClient")
    public CloseableHttpClient closeableHttpClient(@Qualifier("springSslContext") SSLContext sslContext) {
		CloseableHttpClient httpclient = HttpClients.custom()
        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext)
                        .build())
                .build())
        .build();

		return httpclient;
    }

	@Bean
	public ConfigServicePropertySourceLocator configServicePropertySourceLocator(@Qualifier("closeableHttpClient") HttpClient closeableHttpClient) throws Exception {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
		ConfigServicePropertySourceLocator configServicePropertySourceLocator = new ConfigServicePropertySourceLocator(properties);
		configServicePropertySourceLocator.setRestTemplate(new RestTemplate(requestFactory));
		return configServicePropertySourceLocator;
	}
}