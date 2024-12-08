package io.devopsnextgenx.base.modules.aws.dynamodb.config;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import java.io.IOException;
import org.springframework.core.env.PropertiesPropertySource;


public class YamlPropertyLoaderFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), factory.getObject());
    }
}