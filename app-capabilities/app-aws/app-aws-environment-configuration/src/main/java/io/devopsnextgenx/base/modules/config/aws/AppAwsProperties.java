package io.devopsnextgenx.base.modules.config.aws;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * AppSwaggerProperties:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/12/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppAwsProperties {

    @NotBlank
    private String region;
    private String credentialsClass;
    private String bucketName;
    private String storageName;

}
