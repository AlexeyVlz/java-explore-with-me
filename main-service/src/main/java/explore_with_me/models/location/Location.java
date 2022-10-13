package explore_with_me.models.location;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class Location {

    @NotNull @Positive
    Double lat;
    @NotNull @Positive
    Double lon;
}
