package explore_with_me.models.event;

import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.location.Location;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class NewEventDto {

    @NotNull @Length(min = 20, max = 2000)
    String annotation;
    @NotNull @Positive
    CategoryDto categoryDto;
    @NotNull @Length(min = 20, max = 7000)
    String description;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    @NotNull @Length(min = 3, max = 120)
    String title;
}
