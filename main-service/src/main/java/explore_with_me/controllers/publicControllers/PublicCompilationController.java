package explore_with_me.controllers.publicControllers;

import explore_with_me.models.compilation.CompilationDto;
import explore_with_me.services.publicServices.PublicCompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Validated
@Slf4j
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @Autowired
    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

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
