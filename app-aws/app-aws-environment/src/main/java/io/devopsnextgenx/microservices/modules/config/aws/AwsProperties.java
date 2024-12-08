package io.devopsnextgenx.microservices.modules.config.aws;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Validated
public class AwsProperties {

    @NotBlank
    private String region;
    private String credentialsClass;
    private String bucketName;
    private String storageName;
    private String defaultFolderName;
}

