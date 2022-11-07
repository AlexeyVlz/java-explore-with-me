package explore.with.me.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDataNotFound(final DataNotFound e) {
        ApiError apiError = new ApiError(List.of(Arrays.toString(e.getStackTrace())),
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
        ApiError apiError = new ApiError(List.of(Arrays.toString(e.getStackTrace())),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().toString());
        if (e.getCause() != null) {
            apiError.setReason(e.getCause().toString());
        }
        log.info("Возвращена ошибка: \n" + apiError);
        return apiError;
    }
}
