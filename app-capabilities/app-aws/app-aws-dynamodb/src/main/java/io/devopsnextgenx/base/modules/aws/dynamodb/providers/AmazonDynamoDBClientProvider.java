package io.devopsnextgenx.base.modules.aws.dynamodb.providers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;

/**
 * AmazonDynamoDBClientProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
public interface AmazonDynamoDBClientProvider {
    AmazonDynamoDB provide(AppDynamoDBConfig config) throws AppAwsResourceException;
}
