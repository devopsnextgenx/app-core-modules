package io.devopsnextgenx.microservices.modules.logging.config.advice;

import io.devopsnextgenx.microservices.modules.logging.config.filter.BaseApiLogger;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.exception.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
 * Unintentional Exception Flow
 * where an unexpected exception is thrown it will be caught and logged by default in addition to a message sent to client
 */
@Slf4j
@RestControllerAdvice(basePackages = "io.devopsnextgenx", annotations = RestController.class)
@Order(4)
public class GlobalExceptionHandlerController extends BaseHandlerController {

    public GlobalExceptionHandlerController() {
        super();
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleThrowable(Throwable ex, HttpServletRequest request) {
        return createResponseEntity(ex.toString(), ex, HttpStatus.INTERNAL_SERVER_ERROR,
                AppException.ERROR_CODE.OTHER, request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }
}
