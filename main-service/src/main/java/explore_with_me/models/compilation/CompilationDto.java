package explore_with_me.models.compilation;

import explore_with_me.models.event.EventShortDto;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {

    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
