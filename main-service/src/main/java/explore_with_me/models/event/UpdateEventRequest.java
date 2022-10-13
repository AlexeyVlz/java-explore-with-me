package explore_with_me.models.event;

import explore_with_me.models.category.CategoryDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {

    String annotation;
    CategoryDto categoryDto;
    String description;
    LocalDateTime eventDate;
    @NotNull @Positive
    Integer id;
    Boolean paid;
    Integer participantLimit;
    String title;
}
