package explore.with.me.controllers.adminControllers;

import explore.with.me.models.State;
import explore.with.me.services.adminServices.AdminEventService;
import explore.with.me.UtilClass;
import explore.with.me.models.event.AdminUpdateEventRequest;
import explore.with.me.models.event.EventFullDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@Slf4j
public class AdminEventController {

    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @GetMapping
    public List<EventFullDto> adminGetEvents(@RequestParam List<Long> users,
                                             @RequestParam List<String> states,
                                             @RequestParam List<Long> categories,
                                             @RequestParam String rangeStart,
                                             @RequestParam String rangeEnd,
                                             @RequestParam (name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam (name = "size", defaultValue = "10") @Positive Integer size) {
        /*log.info(String.format(
                "Получен запрос к эндпоинту: GET: /admin/events; users = %s, states = %s, categories = %s, " +
                        "rangeStart = %s, rangeEnd = %s, from = %d, size = %d"
                        + users, states, categories, rangeStart, rangeEnd, from, size));*/
        List<State> stateList = states.stream().map(State::valueOf).collect(Collectors.toList());
        AdminEventRestrictions restrictions = AdminEventRestrictions.builder()
                .users(users)
                .states(stateList)
                .categories(categories)
                .rangeStart(UtilClass.toLocalDateTime(rangeStart))
                .rangeEnd(UtilClass.toLocalDateTime(rangeEnd))
                .from(from)
                .size(size)
                .build();
        return adminEventService.adminGetEvents(restrictions);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long eventId,
                                    @RequestBody AdminUpdateEventRequest request) {
        log.info("Получен запрос к эндпоинту: PUT: /admin/events/{eventId}; eventId = "+ eventId);
        return adminEventService.updateEvent(eventId, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @Positive Long eventId) {
        log.info("Получен запрос к эндпоинту: PATCH: /admin/events/{eventId}/publish; eventId = "+ eventId);
        return adminEventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable @Positive Long eventId) {
        log.info("Получен запрос к эндпоинту: PATCH: /admin/events/{eventId}/reject; eventId = "+ eventId);
        return adminEventService.rejectEvent(eventId);
    }
}


