package io.devopsnextgenx.base.modules.aws.dynamodb.providers;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.AppResourceClientProvider;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.extern.slf4j.Slf4j;

/**
 * AwsLocalDynamoDBProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
public class AwsLocalDynamoDBProvider extends AppResourceClientProvider implements AmazonDynamoDBClientProvider {
    @Override
    public AmazonDynamoDB provide(AppDynamoDBConfig config) throws AppAwsResourceException {
        AppResourceClientProvider.AppResource resourceDetails = getResourceDetails(config);
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getPort()),
                        config.getRegion())
                )
                .build();
        log.info("Local Dynamo DB client was created at port '{}' .", resourceDetails.getPort());
        return dynamoDB;
    }
}
