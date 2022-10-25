package exploreWithMe.services.privateServices;

import exploreWithMe.exeption.ConflictDataException;
import exploreWithMe.exeption.DataNotFound;
import exploreWithMe.models.State;
import exploreWithMe.models.event.Event;
import exploreWithMe.models.request.ParticipationRequestDto;
import exploreWithMe.models.request.Request;
import exploreWithMe.models.request.RequestMapper;
import exploreWithMe.models.user.User;
import exploreWithMe.repositories.RequestRepository;
import exploreWithMe.services.adminServices.UserService;
import exploreWithMe.services.publicServices.PublicEventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateRequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final PublicEventService publicEventService;


    public PrivateRequestService(RequestRepository requestRepository, UserService userService, PublicEventService publicEventService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.publicEventService = publicEventService;
    }

    public List<ParticipationRequestDto> getRequests(Long userId) {
        userService.getUserById(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userService.getUserById(userId);
        Event event = checkRequest(userId, eventId);
        Request request = new Request(LocalDateTime.now(), eventId, requester);
        if(event.getRequestModeration()) {
            request.setStatus(State.PENDING);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } else {
                request.setStatus(State.PUBLISHED);
                return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        }
    }

    public ParticipationRequestDto deleteRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFound("Запрос на участие с id = %d в базе не обнаружен"));
        requestRepository.deleteById(requestId);
        return RequestMapper.toParticipationRequestDto(request);
    }

    private Event checkRequest(Long userId, Long eventId){
        if(!requestRepository.findByEventAndRequester(userId, eventId).isEmpty()){
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
        if(!event.getState().equals(State.PUBLISHED)){
            throw new ConflictDataException(
                    "Нельзя подать заявку на неопубликованное событие. Статус события" + event.getState());
        }
        if(event.getConfirmedRequests() >= event.getParticipantLimit()){
            throw new ConflictDataException(String.format(
                    "Лимит заявок на событие id = %d исчерпан. ConfirmedRequests =  %d, ParticipantLimit = %d",
                    eventId, event.getConfirmedRequests(), event.getParticipantLimit()));
        }
        return event;
    }
}
