package io.devopsnextgenx.microservices.modules.logging.config.filter;

import io.devopsnextgenx.microservices.modules.access.model.AccessData;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TomcatApiLoggerFilter extends BaseApiLogger implements Filter {

    private final String applicationName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterchain) throws IOException, ServletException {
        log.info("TomcatApiLoggerFilter.doFilter()");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String accessDataString = request.getHeader(ACCESS_DATA_HEADER);
        String originator = request.getHeader(ORIGINATOR_SERVICE_HEADER);
        // AccessData and other user/org and similar details can be populated once available
        AccessData accessData = AccessData.fromString(accessDataString);


        String CORRELATION_ID = request.getHeader(BaseApiLogger.CORRELATION_ID);
        LoggerConfig loggerConfig = LoggerConfig.builder()
                .uid(Optional.ofNullable(accessData)
                        .map(accessData1 -> accessData1.getUserId())
                        .orElse(null))
                .orgId(Optional.ofNullable(accessData)
                        .map(accessData1 -> accessData1.getOrganizationId())
                        .orElse(null))
                .companyId(Optional.ofNullable(accessData)
                        .map(accessData1 -> accessData1.getCompanyId())
                        .orElse(null))
                .forwardIp(request.getHeader(IP_FORWARD_HEADER))
                .service(applicationName)
                .originator(originator)
                .method(request.getMethod())
                .path(extractFullPathAndQueryParams(request))
                .correlationId(CORRELATION_ID)
                .build();

        GlobalTracer.get().activeSpan().setTag("CID", CORRELATION_ID);

        preFilter(loggerConfig, false);

        filterchain.doFilter(request, response);
        postFilter(response.getStatus());
    }

    @Override
    public void destroy() {
        MDC.clear();
    }

    private String extractFullPathAndQueryParams(HttpServletRequest request) {
        return request.getRequestURI() + Optional.ofNullable(request.getQueryString())
                .map(query -> "?" + query)
                .orElse("");
    }
}
