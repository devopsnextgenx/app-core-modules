package io.devopsnextgenx.microservices.modules.exception;


import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * App exception for error handling in server and client side.
 */
@Getter
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 2756568611480689411L;
    private ERROR_CODE errorCode;
    private HttpStatus status;

    /**
     * Create an exception with {@link ERROR_CODE} and its corresponding {@link HttpStatus}
     *
     * @param errorId the relevant {@link ERROR_CODE} of this exception
     * @param msg     can contains {@link String#format(String, Object...)} variables
     * @param args    the variables for the pattern and optionally last variable { @link Throwable} that is the root cause.
     */
    public AppException(ERROR_CODE errorId, String msg, Object... args) {
        this(errorId, errorId.httpStatus, msg, args);
    }

    /**
     * Create an exception with {@link ERROR_CODE} and its corresponding {@link HttpStatus}
     *
     * @param errorId the relevant {@link ERROR_CODE} of this exception
     * @param status  override the {@link ERROR_CODE#status} with an explicit one.
     * @param msg     can contains {@link String#format(String, Object...)} variables
     * @param args    the variables for the pattern and optionally last variable { @link Throwable} that is the root cause.
     */
    public AppException(ERROR_CODE errorId, @NonNull HttpStatus status, String msg, Object... args) {
        super(args == null || args.length == 0 || (args.length == 1 && findThrowableLastElement(args) != null) ? msg : String.format(msg, args), findThrowableLastElement(args));
        this.errorCode = errorId;
        this.status = status;
    }

    /**
     * please put exception param as last parameter of the constructor varargs.
     *
     * @see this#AppException(ERROR_CODE, String, Object...)
     */
    @Deprecated
    public AppException(Throwable e, AppException.ERROR_CODE errorId, String msg, Object... args) {
        this(errorId, msg, addParamToVarargs(args, e));
    }

    private static Throwable findThrowableLastElement(Object[] args) {
        return Optional.ofNullable(args)
                .filter(argz -> argz.length > 0)
                .map(argz -> argz[argz.length - 1])
                .filter(obj -> Throwable.class.isAssignableFrom(obj.getClass()))
                .map(Throwable.class::cast)
                .orElse(null);
    }

    private static Object[] addParamToVarargs(Object[] args, Throwable e) {
        Object[] newArr = new Object[args.length + 1];
        System.arraycopy(args, 0, newArr, 0, args.length);
        newArr[args.length] = e;

        return newArr;
    }

    public enum ERROR_CODE {

        BAD_REQUEST("0400", HttpStatus.BAD_REQUEST),
        API_UNAUTHORIZED("0401", HttpStatus.UNAUTHORIZED),
        FORBIDDEN_AUTH_METHOD("0402", HttpStatus.FORBIDDEN),
        ACCESS_DENIED("0403", HttpStatus.UNAUTHORIZED),
        UNPROCESSABLE_ENTITY("0422", HttpStatus.UNAUTHORIZED),
        OTHER("9999");

        private final String name;
        private HttpStatus httpStatus;

        ERROR_CODE(String code) {
            name = code;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ERROR_CODE(String s, HttpStatus httpStatus) {
            name = s;
            this.httpStatus = httpStatus;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}