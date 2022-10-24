package explore_with_me.controllers.adminControllers;

import explore_with_me.models.compilation.CompilationDto;
import explore_with_me.models.compilation.NewCompilationDto;
import explore_with_me.services.adminServices.AdminCompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
@Slf4j
public class AdminCompilationsController {

    private final AdminCompilationService compilationService;

    public AdminCompilationsController(AdminCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto addNewCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос к эндпоинту: POST: /admin/compilations; newCompilationDto = " + newCompilationDto);
        return compilationService.addNewCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilationById(@PathVariable @Positive Long compId) {
        log.info("Получен запрос к эндпоинту: DELETE: /admin/compilations/{compId}; compId = " + compId);
        compilationService.deleteCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable @Positive Long compId,
                                         @PathVariable @Positive Long eventId) {
        log.info(String.format("Получен запрос к эндпоинту: DELETE: /admin/compilations/{compId}/events/{eventId}; " +
                "compId = %d, eventId = %d", compId, eventId));
        compilationService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping ("/{compId}/events/{eventId}")
    public void addEventInCompilation (@PathVariable @Positive Long compId,
                                       @PathVariable @Positive Long eventId) {
        log.info(String.format("Получен запрос к эндпоинту: PATCH: /admin/compilations/{compId}/events/{eventId}; " +
                "compId = %d, eventId = %d", compId, eventId));
        compilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deletePin(@PathVariable @Positive Long compId) {
        log.info("Получен запрос к эндпоинту: DELETE: /admin/compilations/{compId}/pin; " +
                "compId = %d, eventId = %d" + compId);
        compilationService.deletePin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void toPin(@PathVariable @Positive Long compId) {
        log.info("Получен запрос к эндпоинту: PATCH: /admin/compilations/{compId}/pin; " +
                "compId = %d, eventId = %d" + compId);
        compilationService.toPin(compId);
    }

}
