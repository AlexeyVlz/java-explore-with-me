package exploreWithMe.models.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewCategoryDto {

    @NotBlank @NotNull
    private String name;
}
