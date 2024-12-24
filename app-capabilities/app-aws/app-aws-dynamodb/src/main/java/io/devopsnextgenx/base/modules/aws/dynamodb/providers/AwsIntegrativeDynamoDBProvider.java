package io.devopsnextgenx.base.modules.aws.dynamodb.providers;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * AwsIntegrativeDynamoDBProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@RequiredArgsConstructor
public class AwsIntegrativeDynamoDBProvider implements AmazonDynamoDBClientProvider {

    private final AWSCredentialsProvider credentials;
    private final AppAwsProperties awsProperties;

    public AmazonDynamoDB provide(AppDynamoDBConfig config) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AmazonDynamoDBException("Parameter 'appx.modules.dynamodb.region' must be specified in integrative configuration")))
                .build();
        log.info("An integrative Dynamo DB client was created");
        return dynamoDB;
    }
}
