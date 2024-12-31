package io.devopsnextgenx.microservices.modules.database.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.client.MongoClient;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application.yml")
@TestPropertySource(properties = { "appx.modules.db.mongodb.enabled = true" })
@ContextConfiguration(classes = {MongoDbConfiguration.class, MongoClientConfiguration.class})
public class MongoClientConfigurationTest {

    @Autowired
    private MongoClient mongoClient;
    
    @Test
    public void testConfigurationProperties() {
      mongoClient.listDatabaseNames()
        .forEach(System.out::println);
    }
}
