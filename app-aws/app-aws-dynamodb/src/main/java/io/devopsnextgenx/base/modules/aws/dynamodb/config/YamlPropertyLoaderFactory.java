package io.devopsnextgenx.base.modules.aws.dynamodb.config;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.env.PropertiesPropertySource;


public class YamlPropertyLoaderFactory implements PropertySourceFactory {

    @Override
    @SuppressWarnings("null")
    public @NonNull PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource encodedResource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties properties = factory.getObject();
        if (properties != null) {
            return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
        }
        throw new IOException("Failed to load YAML properties from resource");
    }
}