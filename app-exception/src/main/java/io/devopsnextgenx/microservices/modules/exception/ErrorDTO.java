package io.devopsnextgenx.microservices.modules.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private final String apiURL;
    private final String correlationID;
    private final String message;
    private final String cause;
    private final String code;
    private final String httpCode;
    private final long timestamp;
}
