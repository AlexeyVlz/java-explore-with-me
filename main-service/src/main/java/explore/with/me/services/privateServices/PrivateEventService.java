package explore.with.me.services.privateServices;

import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.State;
import explore.with.me.models.category.Category;
import explore.with.me.models.category.CategoryMapper;
import explore.with.me.models.event.*;
import explore.with.me.models.likes.Like;
import explore.with.me.models.request.RequestStatus;
import explore.with.me.models.user.User;
import explore.with.me.repositories.LikeRepository;
import explore.with.me.repositories.RequestRepository;
import explore.with.me.services.adminServices.UserService;
import explore.with.me.services.publicServices.PublicCategoryService;
import explore.with.me.exeption.ConflictDataException;
import explore.with.me.models.request.ParticipationRequestDto;
import explore.with.me.models.request.Request;
import explore.with.me.models.request.RequestMapper;
import explore.with.me.repositories.LocationRepository;
import explore.with.me.repositories.eventRepositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventService {

    private final UserService userService;
    private final EventRepository eventRepository;
    private final PublicCategoryService publicCategoryService;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;

    public List<EventShortDto> getEventListByUserId(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Event event = findEventById(updateEventRequest.getEventId());
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictDataException("Изменить данные события может только пользователь, создавший его, " +
                    "либо администратор");
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(CategoryMapper.toCategory(
                    publicCategoryService.getCategoryDtoById(updateEventRequest.getCategory())));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getUserById(userId);
        Category category = publicCategoryService.findCategoryById(newEventDto.getCategory());
        newEventDto.setLocation(locationRepository.save(newEventDto.getLocation()));
        Event event = EventMapper.toEvent(newEventDto, category, initiator);
        event.setLikeCount(0L);
        event.setDislikeCount(0L);
        event = eventRepository.save(event);
        event.setViews(0);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        String errorMessage = "Получить полную информацию о событии может только его инициатор." +
                "Id пользователя, запрашивающего данные не совпадает с id инициатора события";
        checkAccess(event.getInitiator().getId(), userId, errorMessage);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        String errorMessage = "Отменять событие может только его инициатр." +
                "Id пользователя, запрашивающего данные не совпадает с id инициатора события";
        checkAccess(event.getInitiator().getId(), userId, errorMessage);
        if (event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
            eventRepository.save(event);
            return EventMapper.toEventFullDto(event);
        } else {
            throw new ConflictDataException(
                    "Отменить событие можно только со статусом PENDING. Статус данного события - " + event.getState());
        }
    }

    public List<ParticipationRequestDto> getRequestsByEventId(Long userId, Long eventId) {
        String errorMessage = "Видеть список заявок может только инициатор собития" +
                "Id пользователя, запрашивающего данные не совпадает с id инициатора события";
        Event event = findEventById(eventId);
        checkAccess(event.getInitiator().getId(), userId, errorMessage);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public ParticipationRequestDto confirmOrRejectRequest(Long userId, Long eventId, Long reqId, boolean confirm) {
        Event event = findEventById(eventId);
        if (!event.getRequestModeration()) {
            throw new ConflictDataException("Заявки на участие в данном меропирятии не требуют подтверждения " +
                    "инициатора события. RequestModeration = false");
        }
        String errorMessage = "Подтвержлать заявку на участие может только инициатр события." +
                "Id пользователя, подтверждающего заявку не совпадает с id инициатора события";
        checkAccess(event.getInitiator().getId(), userId, errorMessage);
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new DataNotFound(
                String.format("Заявка с id %d в событии: \"%s\" не обнаружено", reqId, event.getTitle())));
        if (confirm) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    public EventFullDto addLikeOrDislike(Long userId, Long eventId, Boolean isLike) {
        userService.getUserById(userId);
        Event event = findEventById(eventId);
        Like like = likeRepository.findLike(userId, eventId);
        if (like == null) {
            return EventMapper.toEventFullDto(saveNewLike(userId, eventId, isLike, event));
        } else {
            return EventMapper.toEventFullDto(updateLike(like, event, isLike));
        }
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено", eventId)));
    }

    private void checkAccess(Long initiatorId, Long userId, String errorMessage) {
        if (!Objects.equals(initiatorId, userId)) {
            throw new ConflictDataException(errorMessage);
        }
    }

    private Event saveNewLike(Long userId, Long eventId, Boolean isLike, Event event) {
        likeRepository.save(new Like(userId, eventId, isLike));
        if (isLike) {
            event.setLikeCount(event.getLikeCount() + 1);
        } else {
            event.setDislikeCount(event.getDislikeCount() + 1);
        }
        return eventRepository.save(event);
    }

    private Event updateLike(Like like, Event event, Boolean isLike) {
        if(like.getIsLike() == isLike) {
            return event;
        }
        if(isLike){
            like.setIsLike(true);
            likeRepository.save(like);
            event.setLikeCount(event.getLikeCount() + 1);
            event.setDislikeCount(event.getDislikeCount() - 1);
        } else {
            like.setIsLike(false);
            likeRepository.save(like);
            event.setLikeCount(event.getLikeCount() - 1);
            event.setDislikeCount(event.getDislikeCount() + 1);
        }
        return eventRepository.save(event);
    }
}
