package explore_with_me.services;

import explore_with_me.exeption.ConflictDataException;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.category.Category;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.models.event.*;
import explore_with_me.models.user.User;
import explore_with_me.repositories.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final UserService userService;
    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    public EventService(UserService userService, EventRepository eventRepository, CategoryService categoryService) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
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
            throw new ConflictDataException("Изменить данные события может только пользователь создавший его, " +
                    "либо администратор");
        }
        if(updateEventRequest.getAnnotation() != null){
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if(updateEventRequest.getCategoryDto() != null) {
            event.setCategory(CategoryMapper.toCategory(updateEventRequest.getCategoryDto()));
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
        Category category = categoryService.findOrCreateCategory(newEventDto.getCategoryDto());
        Event event = EventMapper.toEvent(newEventDto, category, initiator);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        if(!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictDataException("Получить полную информацию о событии может только инициатр." +
                    "Id пользователя, запрашивающего данные не совпадает с id инициатора события");
        }
        return EventMapper.toEventFullDto(event);
    }
    private Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено", id)));
    }



}
