package explore.with.me.controllers.publicControllers;

import explore.with.me.models.compilation.CompilationDto;
import explore.with.me.services.publicServices.PublicCompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam (required = false)Boolean pinned,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam (name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.format("Получен запрос по эндпоинту GET /compilations; pinned = %s, from = %d, size = %d",
                pinned, from, size));
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Positive Long compId) {
        log.info("Получен запрос по эндпоинту GET /compilations/{compId}; compId = " + compId);
        return publicCompilationService.getCompilationById(compId);
    }
}
