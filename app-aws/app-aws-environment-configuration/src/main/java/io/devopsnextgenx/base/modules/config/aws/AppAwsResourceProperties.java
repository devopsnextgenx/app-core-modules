package io.devopsnextgenx.base.modules.config.aws;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AppAwsResourceProperties {
    private String arn;
    private String region;
    private String environmentPrefix;
    private boolean local;
    private Development development;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Development {
        private String modelsPackage;
        private String bucketName;
        private String hostName;
        private int httpPort = 8000;

    }
}
