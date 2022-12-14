package explore.with.me.models.compilation;

import explore.with.me.models.event.EventShortDto;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {

    private Long id;
    private String title;
    private List<EventShortDto> events;
    private Boolean pinned;

    public CompilationDto(Long id, String title, List<EventShortDto> events, Boolean pinned) {
        this.id = id;
        this.title = title;
        this.events = events;
        this.pinned = pinned;
    }

    public CompilationDto() {
    }
}
