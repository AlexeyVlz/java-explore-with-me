package explore.with.me.exeption;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {

    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(List<String> errors, String message, String status, String timestamp) {
        this.errors = errors;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}
