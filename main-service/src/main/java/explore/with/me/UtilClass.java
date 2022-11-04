package explore.with.me;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilClass {

    public static LocalDateTime toLocalDateTime(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static DateTimeFormatter getFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
