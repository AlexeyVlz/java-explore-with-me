package exploreWithMe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilClass {

    public static LocalDateTime toLocalDateTime(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
