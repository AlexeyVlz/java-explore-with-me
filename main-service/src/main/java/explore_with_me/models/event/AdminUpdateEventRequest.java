package explore_with_me.models.event;

import explore_with_me.models.location.Location;
import lombok.Data;

@Data
public class AdminUpdateEventRequest {

    String annotation;
    Integer category;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
}
