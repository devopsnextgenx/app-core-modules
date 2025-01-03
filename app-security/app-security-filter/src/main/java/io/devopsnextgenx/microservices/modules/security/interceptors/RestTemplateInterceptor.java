package io.devopsnextgenx.microservices.modules.security.interceptors;

import java.io.IOException;
import org.slf4j.MDC;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import io.devopsnextgenx.microservices.modules.security.RequestSecurityContext;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = RequestSecurityContext.getContext().getToken();
        if(token != null && !token.isEmpty()) {
            request.getHeaders().add(RequestSecurityContext.REQUEST_JWT_HEADER_NAME, token);
        }
        
        String accessData = RequestSecurityContext.getContext().getAccessData();
        if(accessData != null && !accessData.isEmpty()) {
            request.getHeaders().add(RequestSecurityContext.REQUEST_ACCESSDATA_HEADER_NAME, accessData);
        }

        String correlationId = RequestSecurityContext.getContext().getCorrelationId();
        if(correlationId != null && !correlationId.isEmpty()) {
            request.getHeaders().add(RequestSecurityContext.REQUEST_CORRELATIONID_HEADER_NAME, correlationId);
        } else {
            request.getHeaders().add(RequestSecurityContext.REQUEST_CORRELATIONID_HEADER_NAME, MDC.get(RequestSecurityContext.REQUEST_CORRELATIONID_HEADER_NAME));
        }
        return execution.execute(request, body);
    }
}