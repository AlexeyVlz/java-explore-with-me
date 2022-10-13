package explore_with_me.models.event;

import explore_with_me.models.State;
import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.location.Location;
import explore_with_me.models.user.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventFullDto {

    Integer id;
    String annotation;
    CategoryDto categoryDto;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    String publishedOn;
    Boolean requestModeration;
    State state;
    String title;
    Integer views;
}
