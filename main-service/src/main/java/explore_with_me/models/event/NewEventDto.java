package explore_with_me.models.event;

import explore_with_me.models.location.Location;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class NewEventDto {

    @NotNull @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull @Positive
    private Long category;
    @NotNull @Length(min = 20, max = 7000)
    private String description;
    @NotNull @NotBlank
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull @Length(min = 3, max = 120)
    private String title;
}
