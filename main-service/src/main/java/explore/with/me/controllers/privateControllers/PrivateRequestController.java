package explore.with.me.controllers.privateControllers;

import explore.with.me.models.request.ParticipationRequestDto;
import explore.with.me.services.privateServices.PrivateRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Validated
@Slf4j
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;


    @Autowired
    public PrivateRequestController(PrivateRequestService privateRequestService) {
        this.privateRequestService = privateRequestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable @Positive Long userId) {
        log.info("Получен запрос к эндпоинту: GET /users/{userId}/requests; userId = " + userId);
        return privateRequestService.getRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long userId,
                                              @RequestParam @Positive Long eventId) {
        log.info(String.format("Получен запрос к эндпоинту: POST /users/{userId}/requests; userId = %d, eventId = %d",
                userId, eventId));
        return privateRequestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    private ParticipationRequestDto cancelRequest(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long requestId) {
        log.info(String.format("Получен запрос к эндпоинту: PATCH /users/{userId}/requests/{requestId}/cancel; " +
                "userId = %d, requestId = %d", userId, requestId));
        return privateRequestService.cancelRequest(userId, requestId);
    }


}
