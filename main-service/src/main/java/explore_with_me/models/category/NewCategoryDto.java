package explore_with_me.models.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewCategoryDto {

    @NotBlank
    String name;
}
