package explore_with_me.exeption;

public class DataNotFound extends RuntimeException {
    public DataNotFound(String message) {
        super(message);
    }
}
