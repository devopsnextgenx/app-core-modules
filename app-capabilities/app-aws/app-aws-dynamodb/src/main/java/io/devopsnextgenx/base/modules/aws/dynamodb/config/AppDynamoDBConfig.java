package io.devopsnextgenx.base.modules.aws.dynamodb.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.devopsnextgenx.base.modules.config.aws.AppAwsResourceProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppDynamoDBConfig:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppDynamoDBConfig extends AppAwsResourceProperties {
    private DynamoDBMapperConfig.SaveBehavior saveBehavior = DynamoDBMapperConfig.SaveBehavior.PUT;
}
