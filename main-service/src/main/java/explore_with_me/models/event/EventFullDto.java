package explore_with_me.models.event;

import explore_with_me.models.State;
import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.location.Location;
import explore_with_me.models.user.UserShortDto;
import lombok.Data;


@Data
public class EventFullDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Integer views;

    public EventFullDto(Long id, String annotation, CategoryDto category, Integer confirmedRequests,
                        String createdOn, String description, String eventDate, UserShortDto initiator,
                        Location location, Boolean paid, Integer participantLimit, Boolean requestModeration,
                        State state, String title, Integer views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }
}
