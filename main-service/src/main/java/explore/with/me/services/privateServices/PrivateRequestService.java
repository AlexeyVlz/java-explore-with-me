package explore.with.me.services.privateServices;

import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.event.Event;
import explore.with.me.models.request.RequestStatus;
import explore.with.me.models.user.User;
import explore.with.me.services.adminServices.UserService;
import explore.with.me.services.publicServices.PublicEventService;
import explore.with.me.exeption.ConflictDataException;
import explore.with.me.models.State;
import explore.with.me.models.request.ParticipationRequestDto;
import explore.with.me.models.request.Request;
import explore.with.me.models.request.RequestMapper;
import explore.with.me.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final PublicEventService publicEventService;

    public List<ParticipationRequestDto> getRequests(Long userId) {
        userService.getUserById(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userService.getUserById(userId);
        Event event = checkRequest(userId, eventId);
        Request request = new Request(LocalDateTime.now(), eventId, requester);
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        }
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFound("Запрос на участие с id = %d в базе не обнаружен"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private Event checkRequest(Long userId, Long eventId) {
        if (!requestRepository.findByEventAndRequester(userId, eventId).isEmpty()) {
            throw new ConflictDataException(String.format(
                    "Заявка на событие id = %d от пользователь id = %d уже подавалась.", eventId, userId));
        }
        Event event = publicEventService.findEventById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictDataException(String.format(
                    "Пользователь не может подать заявку на участие в своем событии. " +
                            "Id пользователя подающего заявку - %d, id инициатора события - %d",
                    userId, event.getInitiator().getId()));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictDataException(
                    "Нельзя подать заявку на неопубликованное событие. Статус события" + event.getState());
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictDataException(String.format(
                    "Лимит заявок на событие id = %d исчерпан. ConfirmedRequests =  %d, ParticipantLimit = %d",
                    eventId, event.getConfirmedRequests(), event.getParticipantLimit()));
        }
        return event;
    }
}
