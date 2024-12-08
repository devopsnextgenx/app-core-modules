package io.devopsnextgenx.microservices.modules.logging.config.advice;

import io.devopsnextgenx.microservices.modules.logging.config.filter.BaseApiLogger;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.exception.ErrorDTO;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Handle application global aspect of exception handling, logging and error response.
 * <p>
 * Intentional Exception Flow
 * <p>
 * Where a RestClient specific exception is thrown E.g {@link RestClientResponseException} its an indication to fail the request
 * and return an error message to the user.
 * In case the exception contains a cause, it will be logged as well.
 * <p>
 */
@Slf4j
@RestControllerAdvice(basePackages = "io.devopsnextgenx", annotations = RestController.class)
@Order(3)
public class RestClientExceptionHandlerController extends BaseHandlerController {

    public RestClientExceptionHandlerController(Tracer tracer) {
        super(tracer);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        MethodParameter methodParameter = exception.getParameter();
        String message = String.format("Missing required parameter in request. Method: [%s], Parameter[%s:%s]", methodParameter.getMethod().getName(), methodParameter.getParameter().getParameterizedType().getTypeName(), methodParameter.getParameter().getName());
        return createResponseEntity(message, exception, HttpStatus.BAD_REQUEST, AppException.ERROR_CODE.OTHER, request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }

    @ExceptionHandler(value = MissingPathVariableException.class)
    public ResponseEntity<ErrorDTO> handleMissingPathVariableException(MissingPathVariableException exception, HttpServletRequest request) {
        String message = String.format("Missing path variable parameter in request. Variable: [%s]", exception.getVariableName());
        return createResponseEntity(message, exception, HttpStatus.BAD_REQUEST, AppException.ERROR_CODE.OTHER, request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }

    @ExceptionHandler(value = MissingMatrixVariableException.class)
    public ResponseEntity<ErrorDTO> handleMissingMatrixVariableException(MissingMatrixVariableException exception, HttpServletRequest request) {
        String message = String.format("Missing matrix variable parameter in request. Variable: [%s]", exception.getVariableName());
        return createResponseEntity(message, exception, HttpStatus.BAD_REQUEST, AppException.ERROR_CODE.OTHER, request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }

    @ExceptionHandler(value = RestClientResponseException.class)
    public ResponseEntity<ErrorDTO> handleHttpStatusCodeException(RestClientResponseException exception, HttpServletRequest request) {
        return createResponseEntity(exception.getResponseBodyAsString(), exception, HttpStatus.resolve(exception.getRawStatusCode()), AppException.ERROR_CODE.OTHER, request, MDC.get(BaseApiLogger.CORRELATION_ID));
    }
}
