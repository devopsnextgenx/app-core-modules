package io.devopsnextgenx.base.modules.aws.s3.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.devopsnextgenx.base.modules.config.aws.AppAwsResourceProperties;
import lombok.Data;

/**
 * AppS3Config:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppS3Config extends AppAwsResourceProperties {

}
