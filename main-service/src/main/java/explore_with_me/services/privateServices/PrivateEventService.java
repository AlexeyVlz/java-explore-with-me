package explore_with_me.services.privateServices;

import explore_with_me.exeption.ConflictDataException;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.State;
import explore_with_me.models.category.Category;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.models.event.*;
import explore_with_me.models.request.ParticipationRequestDto;
import explore_with_me.models.request.Request;
import explore_with_me.models.request.RequestMapper;
import explore_with_me.models.user.User;
import explore_with_me.repositories.LocationRepository;
import explore_with_me.repositories.eventRepositories.EventRepository;
import explore_with_me.repositories.RequestRepository;
import explore_with_me.services.adminServices.UserService;
import explore_with_me.services.publicServices.PublicCategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PrivateEventService {

    private final UserService userService;
    private final EventRepository eventRepository;

    private final PublicCategoryService publicCategoryService;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    public PrivateEventService(UserService userService, EventRepository eventRepository,
                               PublicCategoryService publicCategoryService, RequestRepository requestRepository, LocationRepository locationRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.publicCategoryService = publicCategoryService;
        this.requestRepository = requestRepository;
        this.locationRepository = locationRepository;
    }

    public List<EventShortDto> getEventListByUserId(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        int page = from/size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Event event = findEventById(updateEventRequest.getEventId());
        if(!event.getInitiator().getId().equals(userId)) {
            throw new ConflictDataException("Изменить данные события может только пользователь, создавший его, " +
                    "либо администратор");
        }
        if(updateEventRequest.getAnnotation() != null){
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if(updateEventRequest.getCategory() != null) {
            event.setCategory(CategoryMapper.toCategory(
                    publicCategoryService.getCategoryDtoById(updateEventRequest.getCategory())));
        }
        if(updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if(updateEventRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if(updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if(updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userService.getUserById(userId);
        Category category = publicCategoryService.findCategoryById(newEventDto.getCategory());
        newEventDto.setLocation(locationRepository.save(newEventDto.getLocation()));
        Event event = EventMapper.toEvent(newEventDto, category, initiator);
        return EventMapper.toEventFullDto(eventRepository.save(event));
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
        if(event.getState().equals(State.PENDING)){
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
        if(!event.getRequestModeration()){
            throw new ConflictDataException("Заявки на участие в данном меропирятии не требуют подтверждения " +
                    "инициатора события. RequestModeration = false");
        }
        String errorMessage = "Подтвержлать заявку на участие может только инициатр события." +
                "Id пользователя, подтверждающего заявку не совпадает с id инициатора события";
        checkAccess(event.getInitiator().getId(), userId, errorMessage);
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new DataNotFound(
                String.format("Заявка с id %d в событии: \"%s\" не обнаружено", reqId, event.getTitle())));
        if (confirm) {
            request.setStatus(State.PUBLISHED);
        } else {
            request.setStatus(State.CANCELED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено", eventId)));
    }

    private void checkAccess(Long initiatorId, Long userId, String errorMessage) {
        if(!Objects.equals(initiatorId, userId)) {
            throw new ConflictDataException(errorMessage);
        }
    }


}
