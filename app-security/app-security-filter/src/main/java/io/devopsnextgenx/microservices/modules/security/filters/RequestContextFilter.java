package io.devopsnextgenx.microservices.modules.security.filters;

import java.io.IOException;

import io.devopsnextgenx.microservices.modules.security.RequestSecurityContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
        RequestSecurityContext.getContext().setToken(extractAndSetRequestContext(httpServletRequest, RequestSecurityContext.REQUEST_JWT_HEADER_NAME));
        RequestSecurityContext.getContext().setAccessData(extractAndSetRequestContext(httpServletRequest, RequestSecurityContext.REQUEST_ACCESSDATA_HEADER_NAME));
        
        chain.doFilter(request, response);
    }

    private String extractAndSetRequestContext(HttpServletRequest httpServletRequest, String headerName) {
        String data = httpServletRequest.getHeader(headerName);

        if (data == null || "".equals(data)) {
            log.warn("RequestContextFilter: No {} header found in request", headerName);
        }

        return data;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}