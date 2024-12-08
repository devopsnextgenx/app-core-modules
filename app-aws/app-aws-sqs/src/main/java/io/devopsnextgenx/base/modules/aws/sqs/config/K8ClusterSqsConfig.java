package io.devopsnextgenx.base.modules.aws.sqs.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.devopsnextgenx.base.modules.config.aws.AppAwsResourceProperties;
import lombok.Data;

import java.util.List;

/**
 * AppSqsConfig:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppSqsConfig extends AppAwsResourceProperties {
    private List<String> queueList;
}
