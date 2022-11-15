package explore.with.me.models.compilation;

import explore.with.me.models.event.Event;
import explore.with.me.models.event.EventMapper;
import explore.with.me.models.event.EventShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation(newCompilationDto.getTitle(), events, newCompilationDto.getPinned());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtos = compilation.getEvents()
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return new CompilationDto(compilation.getId(), compilation.getTitle(), eventShortDtos, compilation.getPinned());
    }
}
