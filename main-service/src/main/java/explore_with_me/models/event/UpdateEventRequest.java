package explore_with_me.models.event;

import explore_with_me.models.category.CategoryDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateEventRequest {

    private String annotation;
    private CategoryDto categoryDto;
    private String description;
    private String eventDate;
    @NotNull @Positive
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
