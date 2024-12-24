package io.devopsnextgenx.base.modules.aws.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.AttributeEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DynamoDBEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.providers.DirectKmsMaterialProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.util.StringUtils;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.AwsResourceInitializer;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class BaseDynamoDBMapperInitializer implements AwsResourceInitializer {

    public AppDynamoDBConfig appDynamoDBConfig;
    public AmazonDynamoDB awsDynamoDB;
    public AWSCredentialsProvider awsCredentialsProvider;
    public AppAwsProperties appAwsProperties;
    public DynamoDBMapperConfig mapperConfig;

    public DynamoDBMapper dynamoDBMapper() {

        String envPrefix = Optional.ofNullable(appDynamoDBConfig.getEnvironmentPrefix())
                .map(env -> env + "_")
                .orElse(null);

        mapperConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(appDynamoDBConfig.getSaveBehavior())
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(envPrefix))
                .build();

        AttributeEncryptor encryptor = null;
        if (!StringUtils.isNullOrEmpty(appDynamoDBConfig.getArn())) {
            Objects.requireNonNull(appAwsProperties.getRegion(), "Parameter 'appx.modules.dynamodb.region' must be specified with arn.");

            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withCredentials(awsCredentialsProvider)
                    .withRegion(appAwsProperties.getRegion())
                    .build();

            encryptor = new AttributeEncryptor(DynamoDBEncryptor.getInstance(new DirectKmsMaterialProvider(kmsClient, appDynamoDBConfig.getArn())));
            log.info("Encryption enabled with KMS for DynamoDBMapper");
        } else {
            log.info("Encryption disabled DynamoDBMapper");
        }

        return new DynamoDBMapper(awsDynamoDB, mapperConfig, encryptor);
    }
}
