package explore_with_me.models.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewCompilationDto {

    List<Integer> events;
    private boolean pinned;
    @NotNull @NotBlank
    private String title;
}
