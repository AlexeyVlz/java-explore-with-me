package explore_with_me.models.compilation;

import explore_with_me.models.event.EventShortDto;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {

    List<EventShortDto> events;
    Integer id;
    Boolean pinned;
    String title;
}
