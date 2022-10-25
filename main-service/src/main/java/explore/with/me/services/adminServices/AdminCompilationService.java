package explore.with.me.services.adminServices;

import explore.with.me.exeption.ConflictDataException;
import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.compilation.Compilation;
import explore.with.me.models.compilation.CompilationDto;
import explore.with.me.models.compilation.CompilationMapper;
import explore.with.me.models.compilation.NewCompilationDto;
import explore.with.me.models.event.Event;
import explore.with.me.repositories.CompilationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final AdminEventService adminEventService;

    public AdminCompilationService(CompilationRepository compilationRepository, AdminEventService adminEventService) {
        this.compilationRepository = compilationRepository;
        this.adminEventService = adminEventService;
    }

    public CompilationDto addNewCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            events = new ArrayList<>();
        } else {
            events = adminEventService.getEventsListById(newCompilationDto.getEvents());
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(compilation);
    }

    public void deleteCompilationById(Long compId) {
        getCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    public void deleteEventInCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationById(compId);
        deleteEvent(compilation, eventId);
        compilationRepository.save(compilation);
    }

    public void addEventInCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationById(compId);
        for (Event event : compilation.getEvents()) {
            if (event.getId().intValue() == eventId.intValue()) {
                throw new ConflictDataException(String.format("Событие с id = %d в подборке id = %d уже существует",
                        eventId, compId));
            }
        }
        compilation.getEvents().add(adminEventService.findEventById(eventId));
        compilationRepository.save(compilation);
    }

    public void deletePin(Long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void toPin(Long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    private Compilation deleteEvent(Compilation compilation, Long eventId) {
        adminEventService.findEventById(eventId);
        int check = 0;
        if (compilation.getEvents() != null) {
            for (int i = compilation.getEvents().size() - 1; i >= 0; i--) {
                if (compilation.getEvents().get(i).getId().intValue() == eventId.intValue()) {
                    compilation.getEvents().remove(i);
                    check++;
                }
            }
        }
        if (check == 0) {
            throw new DataNotFound(String.format("Событие с id = %d в подбовке с id = %d не найдено",
                    eventId, compilation.getId()));
        }
        return compilation;
    }

    private Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new DataNotFound(
                "Подборка с id = %d в базе данных не обнаружена" + compId));
    }


}
