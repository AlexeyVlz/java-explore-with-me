package explore.with.me.models.event;

import explore.with.me.models.location.Location;
import lombok.Data;

@Data
public class AdminUpdateEventRequest {

    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
