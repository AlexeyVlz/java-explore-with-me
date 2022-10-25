package explore.with.me.models.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewCompilationDto {

    List<Long> events;
    private Boolean pinned;
    @NotNull @NotBlank
    private String title;
}
