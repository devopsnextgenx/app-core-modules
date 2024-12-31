package io.devopsnextgenx.microservices.modules.database.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;

@Configuration
@ConditionalOnProperty(value = "appx.modules.db.mongodb.enabled", havingValue = "true", matchIfMissing = false)
public class MongoDbConfiguration {
    @Autowired
    private MongoClientConfiguration mongoClientConfiguration;
    
    @Bean
    MongoClient mongoClient() {
        return mongoClientConfiguration.mongoClient();
    }
}
