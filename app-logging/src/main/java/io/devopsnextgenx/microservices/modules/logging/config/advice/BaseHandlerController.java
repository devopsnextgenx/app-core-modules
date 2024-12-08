package io.devopsnextgenx.microservices.modules.logging.config.advice;

import io.devopsnextgenx.microservices.modules.logging.config.filter.BaseApiLogger;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.exception.AppExceptionUtil;
import io.devopsnextgenx.microservices.modules.exception.ErrorDTO;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

public abstract class BaseHandlerController {
    protected Tracer jaegerTracer;

    public BaseHandlerController(Tracer tracer) {
        jaegerTracer = tracer;
    }

    protected void enrichMetricsTrace(Throwable ex, String message) {
        Span activeSpan = jaegerTracer.activeSpan();
        if (Objects.isNull(jaegerTracer) || Objects.isNull(activeSpan)) {
            return;
        }

        if (ex instanceof AppException) {
            activeSpan.setTag("ERROR_CODE", ((AppException) ex).getErrorCode().name());
        }

        Tags.ERROR.set(activeSpan, true);
        activeSpan.setTag("EXCEPTION_TYPE", ex.getClass().getSimpleName());
        activeSpan.setTag("CID", MDC.get(BaseApiLogger.CORRELATION_ID));
        activeSpan.log(StringUtils.isNotEmpty(message) ? message : ex.getMessage());
    }

    protected ResponseEntity<ErrorDTO> createResponseEntity(String message, Throwable exception, HttpStatus status, AppException.ERROR_CODE errorCode, HttpServletRequest request, String correlationId) {
        enrichMetricsTrace(exception, message);
        return AppExceptionUtil.createError(exception.getMessage(), exception, status, errorCode, request, correlationId);
    }


}
