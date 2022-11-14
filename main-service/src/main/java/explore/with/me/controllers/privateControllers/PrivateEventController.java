package explore.with.me.controllers.privateControllers;

import explore.with.me.models.event.*;
import explore.with.me.models.likes.LikeOut;
import explore.with.me.models.request.ParticipationRequestDto;
import explore.with.me.services.privateServices.PrivateEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @GetMapping
    public List<EventShortDto> getEventListByUserId(@PathVariable @Positive Long userId,
                                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero
                                                    Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Positive
                                                    Integer size) {
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
                "Получен запрос к эндпоинту: GET: /users/{userId}/events/{eventId}; userId = %d, eventId = %d",
                userId, eventId));
        return privateEventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}; userId = %d, eventId = %d",
                userId, eventId));
        return privateEventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                              @PathVariable @Positive Long eventId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: GET: /users/{userId}/events/{eventId}/requests; userId = %d, eventId = %d",
                userId, eventId));
        return privateEventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long eventId,
                                                  @PathVariable @Positive Long reqId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}/requests/{reqId}/confirm; " +
                        "userId = %d, eventId = %d, reqId = %d", userId, eventId, reqId));
        return privateEventService.confirmOrRejectRequest(userId, eventId, reqId, true);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId,
                                                 @PathVariable @Positive Long reqId) {
        log.info(String.format(
                "Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}/requests/{reqId}/confirm; " +
                        "userId = %d, eventId = %d, reqId = %d", userId, eventId, reqId));
        return privateEventService.confirmOrRejectRequest(userId, eventId, reqId, false);
    }

    @PatchMapping("/{eventId}/like")
    public LikeOut addLikeOrDislike(@PathVariable @Positive Long userId,
                                         @PathVariable @Positive Long eventId,
                                         @RequestParam Boolean isLike) {
        log.info(String.format("Получен запрос к эндпоинту: PATCH: /users/{userId}/events/{eventId}/like; " +
                "userId = %d, eventId = %d, isLike = %s", userId, eventId, isLike));
        LikeOut like = privateEventService.addLikeOrDislike(userId, eventId, isLike);
        log.info(String.format("По событию с id = %d поставлено лайков:  %d и дизлайков = %d: ",
                like.getEventId(), like.getLikeCount(), like.getDislikeCount()));
        return like;
    }

    @DeleteMapping("/{eventId}/like")
    public void deleteLikeOrDislike(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @RequestParam Boolean isLike) {
        log.info(String.format("Получен запрос к эндпоинту: DELETE: /users/{userId}/events/{eventId}/like; " +
                "userId = %d, eventId = %d, isLike = %s", userId, eventId, isLike));
        privateEventService.deleteLikeOrDislike(userId, eventId, isLike);
    }
}
