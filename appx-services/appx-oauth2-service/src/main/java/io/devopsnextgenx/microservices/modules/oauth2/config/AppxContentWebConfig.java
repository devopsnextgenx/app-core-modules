package io.devopsnextgenx.microservices.modules.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.lang.NonNull;


@Configuration
public class AppxContentWebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Configure public content
        registry.addResourceHandler("/content/public/**")
               .addResourceLocations("classpath:/content/public/")
               .setCacheControl(CacheControl.noCache());
        
        // Configure protected static content
        registry.addResourceHandler("/content/secure/**")
               .addResourceLocations("classpath:/content/secure/")
               .setCacheControl(CacheControl.noCache());
    }
}