package explore_with_me.controllers.privateControllers;

import explore_with_me.models.event.EventFullDto;
import explore_with_me.models.event.EventShortDto;
import explore_with_me.models.event.NewEventDto;
import explore_with_me.models.event.UpdateEventRequest;
import explore_with_me.models.request.ParticipationRequestDto;
import explore_with_me.services.privateServices.PrivateEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
@Slf4j
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @Autowired
    public PrivateEventController(PrivateEventService privateEventService) {
        this.privateEventService = privateEventService;
    }

    @GetMapping
    public List<EventShortDto> getEventListByUserId(@PathVariable @Positive Long userId,
                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam (name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос к эндпоинту: GET: /users/{userId}/events; userId = " + userId);
        return privateEventService.getEventListByUserId(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable @Positive Long userId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Получен запрос к эндпоинту: PATCH: /users/{userId}/events; userId = " + userId);
        return privateEventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto addNewEvent(@PathVariable @Positive Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Получен запрос к эндпоинту: POST: /users/{userId}/events; userId = " + userId);
        return privateEventService.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        log.info(String.format(
            "Получен запрос к эндпоинту: GET: /users/{userId}/events/{eventId}; userId = %d, eventId = %d"
                    + userId, eventId));
        return privateEventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        log.info(String.format(
           "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}; userId = %d, eventId = %d"
                   + userId, eventId));
        return privateEventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                        @PathVariable @Positive Long eventId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: GET: /users/{userId}/events/{eventId}/requests; userId = %d, eventId = %d"
                        + userId, eventId));
        return privateEventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long eventId,
                                                  @PathVariable @Positive Long reqId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}/requests/{reqId}/confirm; " +
                        "userId = %d, eventId = %d, reqId = %d"+ userId, eventId, reqId));
        return privateEventService.confirmOrRejectRequest(userId, eventId, reqId, true);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long eventId,
                                                  @PathVariable @Positive Long reqId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}/requests/{reqId}/confirm; " +
                        "userId = %d, eventId = %d, reqId = %d"+ userId, eventId, reqId));
        return privateEventService.confirmOrRejectRequest(userId, eventId, reqId, false);
    }
}
