package io.devopsnextgenx.microservices.modules.logging.config.filter;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class LoggerConfig {

    private String companyId;
    private String orgId;
    private String uid;
    private String sessionId;
    private String correlationId;
    private String forwardIp;
    private String service;
    private String originator;
    private String method;
    private String path;
    private String status;
    private String duration;
}
