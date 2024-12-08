package io.devopsnextgenx.microservices.modules.security.jwt.helpers;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.List;

// TODO: move to a dedicated spring config module

/**
 * A factory used to load YAML files into Spring @{@link org.springframework.context.annotation.PropertySource} annotations
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        CompositePropertySource propertySource = new CompositePropertySource(name);
        List<PropertySource<?>> propertySources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        propertySources.stream()
                .forEach(propertySource::addPropertySource);
        return propertySource;
    }
}
