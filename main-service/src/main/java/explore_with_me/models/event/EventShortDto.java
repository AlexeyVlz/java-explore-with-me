package explore_with_me.models.event;

import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.user.UserShortDto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventShortDto {

    Integer id;
    String annotation;
    CategoryDto categoryDto;
    Integer confirmedRequests;
    String description;
    LocalDateTime eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Integer views;
}
