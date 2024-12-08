package io.devopsnextgenx.microservices.modules.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.function.Function;

@Slf4j
public class AppExceptionUtil {

    private static HashMap<Class<?>, Function<Throwable, String>> exceptionMsgResolverMap = getMsgResolverFunctionalMap();

    public static String getInnerExceptionData(Throwable e) {
        if (e == null) {
            return ".";
        }
        Function<Throwable, String> exceptionMsgResolver = exceptionMsgResolverMap.getOrDefault(e.getClass(), Throwable::getMessage);
        String returnMsg = e.getClass().getSimpleName() + ": " + exceptionMsgResolver.apply(e);
        if (e.getCause() != null) {
            String innerExceptionData = getInnerExceptionData(e.getCause());
            returnMsg = String.format("%s, caused by   %s", returnMsg, innerExceptionData);
        }
        return returnMsg;
    }

    private static HashMap<Class<?>, Function<Throwable, String>> getMsgResolverFunctionalMap() {
        HashMap<Class<?>, Function<Throwable, String>> excpetionMsgResolverMap = new HashMap<>();
        excpetionMsgResolverMap.put(HttpServerErrorException.class, AppExceptionUtil::getMsgFromBody);
        excpetionMsgResolverMap.put(HttpClientErrorException.class, AppExceptionUtil::getMsgFromBody);
        return excpetionMsgResolverMap;
    }

    private static String getMsgFromBody(Throwable e) {
        String responseBodyAsString;
        if (e instanceof RestClientResponseException) {
            RestClientResponseException localException = (RestClientResponseException) e;
            responseBodyAsString = localException.getResponseBodyAsString();
        } else {
            throw new RuntimeException(String.format("Wrong exception type %s. original inner message: %s", e.getClass().getSimpleName(), e.getMessage()));
        }
        return responseBodyAsString;
    }

    public static ResponseEntity<ErrorDTO> createError(String message, Throwable ex, HttpStatus status, AppException.ERROR_CODE errorCode, HttpServletRequest request, String correlationId) {
        log.error(message, ex);

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message(message)
                .cause(AppExceptionUtil.getInnerExceptionData(ex.getCause()))
                .code(errorCode.toString())
                .timestamp(Instant.now().toEpochMilli())
                .correlationID(correlationId)
                .apiURL(String.format("%s : %s", request.getMethod(), request.getRequestURI()))
                .build();

        return ResponseEntity.status(status).body(errorDTO);
    }
}
