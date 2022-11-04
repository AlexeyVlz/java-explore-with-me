package explore.with.me.controllers.publicControllers;

import explore.with.me.models.event.EventShortDto;
import explore.with.me.services.publicServices.PublicEventService;
import explore.with.me.UtilClass;
import explore.with.me.models.event.EventFullDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventShortDto> getFilteredEvents(@RequestParam String text,
                                                 @RequestParam List<Long> categories,
                                                 @RequestParam Boolean paid,
                                                 @RequestParam String rangeStart,
                                                 @RequestParam String rangeEnd,
                                                 @RequestParam Boolean onlyAvailable,
                                                 @RequestParam String sort,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                 HttpServletRequest request) {
        log.info(String.format("Получен запрос к эндпоинту: GET: /events; " +
                        "text = %s, categories = %s, paid = %s, rangeStart = %s, rangeEnd = %s, onlyAvailable = %s, " +
                        "sort = %s, from = %d, size = %d, httpServletRequest = %s",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request));
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
            return publicEventService.getFilteredEvents(restrictions, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: GET: /events/{id}; id = " + id);
            return publicEventService.getEventById(id, request);
    }
}
