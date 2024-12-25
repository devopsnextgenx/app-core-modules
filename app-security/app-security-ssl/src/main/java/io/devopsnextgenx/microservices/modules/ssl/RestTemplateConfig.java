package io.devopsnextgenx.microservices.modules.ssl;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class RestTemplateConfig {

    public RestTemplate restTemplateWithTrustStore(
            String truststorePath,
            String truststorePassword,
            ResourceLoader resourceLoader) throws Exception {
        
        SSLContext sslContext = createSSLContext(truststorePath, truststorePassword, resourceLoader);
        
        // Create SSL Socket Factory
        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();
        
        // Create Connection Manager
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(20)
                .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                .build();
        
        // Create HTTP Client
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofMilliseconds(TimeUnit.SECONDS.toMillis(30)))
                .build();

        // Create Request Factory
        HttpComponentsClientHttpRequestFactory factory = 
                new HttpComponentsClientHttpRequestFactory(httpClient);
        
        // Configure timeouts
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);
        factory.setReadTimeout(30000);
        
        return new RestTemplate(factory);
    }

    private SSLContext createSSLContext(
            String truststorePath,
            String truststorePassword,
            ResourceLoader resourceLoader) throws Exception {
        
        Resource truststoreResource = resourceLoader.getResource(truststorePath);
        KeyStore truststore = loadTrustStore(truststoreResource, truststorePassword);
        
        return SSLContextBuilder.create()
                .loadTrustMaterial(truststore, null)
                .build();
    }

    private KeyStore loadTrustStore(Resource truststoreResource, String password) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(truststoreResource.getInputStream(), password.toCharArray());
        return keyStore;
    }
}