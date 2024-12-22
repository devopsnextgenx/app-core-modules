package io.devopsnextgenx.microservices.modules.security;

import lombok.Data;

@Data
public class RequestSecurityContext {
    public static final String REQUEST_JWT_HEADER_NAME = "Authorization";
    public static final String REQUEST_ACCESSDATA_HEADER_NAME = "access-data";

    private static final ThreadLocal<RequestSecurityContext> CONTEXT = new ThreadLocal<>();

    private String token;
    private String accessData;

    public static RequestSecurityContext getContext() {
        RequestSecurityContext result = CONTEXT.get();

        if (result == null) {
            result = new RequestSecurityContext();
            CONTEXT.set(result);
        }

        return result;
    }
}
