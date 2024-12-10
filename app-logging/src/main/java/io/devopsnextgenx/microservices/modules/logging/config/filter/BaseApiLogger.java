package io.devopsnextgenx.microservices.modules.logging.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;

@Slf4j
public class BaseApiLogger {

    // HTTP Headers
    public static final String ACCESS_DATA_HEADER = "access-data";
    public static final String ORIGINATOR_SERVICE_HEADER = "ORIGINATOR";
    public static final String CORRELATION_ID = "Correlation-ID";
    public static final String IP_FORWARD_HEADER = "x-forwarded-for";
    // MDC Params
    public static final String MDC_START_TIME = "REQUEST_START_TIME";
    public static final String COMPANY_ID = "COMPANY-ID";
    public static final String ORG_ID = "ORG-ID";
    public static final String UID = "UID";
    public static final String ORIGIN = "Origin";
    public static final String SESSION_ID = "SessionID";
    public static final String FORWARD_IP = "ForwardIP";
    public static final String SERVICE = "Service";
    /**
     * Marker for API log activities.
     */
    public static Marker API_MARKER = MarkerFactory.getMarker("API_LOG");
    public static final String METHOD_NAME = "METHOD";
    public static final String PATH_NAME = "PATH";
    public static final String STATUS_NAME = "Status";
    public static final String DURATION_NAME = "Duration";

    protected void preFilter(LoggerConfig config) {
        MDC.put(MDC_START_TIME, String.valueOf(System.currentTimeMillis()));
        MDC.put(UID, config.getUid());
        MDC.put(ORG_ID, config.getOrgId());
        MDC.put(COMPANY_ID, config.getCompanyId());
        MDC.put(SESSION_ID, config.getSessionId());
        MDC.put(FORWARD_IP, config.getForwardIp());
        MDC.put(SERVICE, config.getService());
        MDC.put(METHOD_NAME, config.getMethod());
        MDC.put(PATH_NAME, config.getPath());
        MDC.put(CORRELATION_ID, config.getCorrelationId());
        MDC.put(ORIGINATOR_SERVICE_HEADER, config.getOriginator());
    }

    protected void postFilter(int status) {
        MDC.put(STATUS_NAME, String.valueOf(status));
        if (MDC.get(MDC_START_TIME) != null) {
            MDC.put(DURATION_NAME, String.valueOf(System.currentTimeMillis() - Long.valueOf(MDC.get(MDC_START_TIME))));
        }
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus != null && httpStatus.is2xxSuccessful()) {
            log.info(API_MARKER, "API_END");
        } else {
            log.error(API_MARKER, "API_END");
        }
    }}
