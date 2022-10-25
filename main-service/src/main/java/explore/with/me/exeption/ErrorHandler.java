package explore.with.me.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())),
                e.getMessage(),
                e.getCause().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().toString());
    }
}
