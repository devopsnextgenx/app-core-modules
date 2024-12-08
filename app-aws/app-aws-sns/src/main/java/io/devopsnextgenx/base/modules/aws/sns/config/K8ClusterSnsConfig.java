package io.devopsnextgenx.base.modules.aws.sns.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.devopsnextgenx.base.modules.config.aws.AppAwsResourceProperties;
import lombok.Data;

import java.util.List;

/**
 * AppSnsConfig:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppSnsConfig extends AppAwsResourceProperties {
    private int cloudFormationOffset = 6;
    private List<String> topicList;
}
