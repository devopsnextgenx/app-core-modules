package io.devopsnextgenx.microservices.modules.logging.config.advice;

import io.devopsnextgenx.microservices.modules.logging.config.filter.BaseApiLogger;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.exception.ErrorDTO;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Handle application global aspect of exception handling, logging and error response.
 * <p>
 * Intentional Exception Flow
 * <p>
 * Where an application specific exception is thrown E.g {@link AppException} its an indication to fail the request
 * and return an error message to the user.
 * In case the exception contains a cause, it will be logged as well.
 * <p>
 */
@Slf4j
@RestControllerAdvice(basePackages = "io.devopsnextgenx", annotations = RestController.class)
@Order(1)
public class AppExceptionHandlerController extends BaseHandlerController {

    public AppExceptionHandlerController() {
        super();
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDTO> handleAppException(AppException exception, HttpServletRequest request) {

        return createResponseEntity(exception.getMessage(), exception, exception.getStatus(), exception.getErrorCode(), request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }
}
