package io.devopsnextgenx.base.modules.aws.dynamodb.integrative;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.devopsnextgenx.base.modules.aws.dynamodb.BaseDynamoDBMapperInitializer;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * DynamoDBMapperInitializer:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
public class DynamoDBIntegrativeInitializer extends BaseDynamoDBMapperInitializer {

    public DynamoDBIntegrativeInitializer(AppDynamoDBConfig appDynamoDBConfig, AmazonDynamoDB awsDynamoDB,
                                            AWSCredentialsProvider awsCredentialsProvider, AppAwsProperties appAwsProperties) {
        this.appDynamoDBConfig = appDynamoDBConfig;
        this.awsDynamoDB = awsDynamoDB;
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.appAwsProperties = appAwsProperties;
    }

    @Override
    public void initializeResources() {
    }
}
