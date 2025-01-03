package io.devopsnextgenx.base.modules.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.devopsnextgenx.base.modules.aws.dynamodb.DynamodbAutoConfiguration;
import io.devopsnextgenx.base.modules.config.YamlPropertyLoaderFactory;
import io.devopsnextgenx.base.modules.config.aws.AwsEnvironmentModuleAutoConfiguration;
import io.devopsnextgenx.base.modules.test.model.MessageTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.context.annotation.PropertySource;

/**
 * IntegrativeDynamoDBTest:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@PropertySource(value = "classpath:application-local.yml", factory = YamlPropertyLoaderFactory.class)
@TestPropertySource(properties = {
    "appx.modules.dynamodb.local = true"
})
@SpringBootTest(classes = {DynamodbAutoConfiguration.class, AwsEnvironmentModuleAutoConfiguration.class})
public class IntegrativeDynamoDBTest {
    @Autowired
    DynamoDBMapper dynamoDBMapper;

    /**
     * Integrates with a predefined table in AWS called "integrative_TestItems"
     */
    @Test
    public void integrativeMapperIT() {
        MessageTest msg = new MessageTest();
        msg.setId("1357");
        msg.setMessage("{'name':'Amit Kshirsagar', 'message':'Hello World!!!'}");
        dynamoDBMapper.save(msg);
        MessageTest messageFromDB = dynamoDBMapper.load(MessageTest.class, "1357");
        Assert.assertEquals("The message retrieved from dynamo DB is not identical to the original item", msg.getMessage(),
                messageFromDB.getMessage());
    }
}
