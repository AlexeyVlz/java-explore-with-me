package explore.with.me.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDataNotFound(final DataNotFound e) {
        StringBuilder errors = new StringBuilder();
        for (StackTraceElement error : e.getStackTrace()) {
            errors.append(error).append("\n");
        }
        ApiError apiError = new ApiError(errors.toString(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString());
        if (e.getCause() != null) {
            apiError.setReason(e.getCause().toString());
        }
        log.info("Возвращена ошибка: \n" + apiError);
        return apiError;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        StringBuilder errors = new StringBuilder();
        for (StackTraceElement error : e.getStackTrace()) {
            errors.append(error).append("\n");
        }
        ApiError apiError = new ApiError(errors.toString(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString());
        if (e.getCause() != null) {
            apiError.setReason(e.getCause().toString());
        }
        log.info("Возвращена ошибка: \n" + apiError);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (StackTraceElement error : e.getStackTrace()) {
            errors.append(error).append("\n");
        }
        ApiError apiError = new ApiError(errors.toString(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString());
        if (e.getCause() != null) {
            apiError.setReason(e.getCause().toString());
        }
        log.info("Возвращена ошибка: \n" + apiError);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        StringBuilder errors = new StringBuilder();
        for (StackTraceElement error : e.getStackTrace()) {
            errors.append(error).append("\n");
        }
        ApiError apiError = new ApiError(errors.toString(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().toString());
        if (e.getCause() != null) {
            apiError.setReason(e.getCause().toString());
        }
        log.info("Возвращена ошибка: \n" + e.getClass());
        log.info("\n" + apiError);
        return apiError;
    }
}
