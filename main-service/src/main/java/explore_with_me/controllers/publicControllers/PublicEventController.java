package explore_with_me.controllers.publicControllers;

import explore_with_me.UtilClass;
import explore_with_me.models.event.EventFullDto;
import explore_with_me.services.publicServices.PublicEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Validated
@Slf4j
public class PublicEventController {

    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    public List<EventFullDto> getFilteredEvents(@RequestParam @NotBlank String text,
                                        @RequestParam List<Long> categories,
                                        @RequestParam Boolean paid,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @RequestParam Boolean onlyAvailable,
                                        @RequestParam String sort,
                                        @RequestParam (name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam (name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.format("Получен запрос к эндпоинту: GET: /events; " +
                "text = %s, categories = %s, paid = %s, rangeStart = %s, rangeEnd = %s, onlyAvailable = %s, " +
                "sort = %s, from = %d, size = %d",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
        PublicEventRestrictions restrictions = PublicEventRestrictions.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(UtilClass.toLocalDateTime(rangeStart))
                .rangeEnd(UtilClass.toLocalDateTime(rangeEnd))
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        return publicEventService.getFilteredEvents(restrictions);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable @Positive Long id) {
        log.info("Получен запрос к эндпоинту: GET: /events/{id}; id = " + id);
        return publicEventService.getEventById(id);
    }
}
