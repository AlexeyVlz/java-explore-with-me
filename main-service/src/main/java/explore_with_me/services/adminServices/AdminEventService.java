package explore_with_me.services.adminServices;

import explore_with_me.controllers.adminControllers.AdminEventRestrictions;
import explore_with_me.exeption.ConflictDataException;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.State;
import explore_with_me.models.category.CategoryMapper;
import explore_with_me.models.event.AdminUpdateEventRequest;
import explore_with_me.models.event.Event;
import explore_with_me.models.event.EventFullDto;
import explore_with_me.models.event.EventMapper;
import explore_with_me.repositories.eventRepositories.EventRepository;
import explore_with_me.services.publicServices.PublicCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEventService {

    private final EventRepository eventRepository;
    private final PublicCategoryService publicCategoryService;

    @Autowired
    public AdminEventService(EventRepository eventRepository, PublicCategoryService publicCategoryService) {
        this.eventRepository = eventRepository;
        this.publicCategoryService = publicCategoryService;
    }

    public List<EventFullDto> adminGetEvents(AdminEventRestrictions restrictions) {
        PageRequest pageRequest = PageRequest.of(restrictions.getFrom()/ restrictions.getSize(),
                restrictions.getSize());
        return eventRepository.adminGetEvents(restrictions, pageRequest)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequest request) {
        Event event = findEventById(eventId);
        if(request.getAnnotation() != null){
            event.setAnnotation(request.getAnnotation());
        }
        if(request.getCategory() != null) {
            event.setCategory(CategoryMapper.toCategory(publicCategoryService.getCategoryDtoById(request.getCategory())));
        }
        if(request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if(request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if(request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if(request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if(request.getRequestModeration() != null){
            event.setRequestModeration(request.getRequestModeration());
        }
        if(request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto publishEvent(Long eventId) {
        Event event = findEventById(eventId);
        if(!event.getState().equals(State.PENDING)){
            throw new ConflictDataException(
                    "Событие должно находиться в статусе PENDING, статус данного события" + event.getState());
        }
        if(event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())){
            throw new ConflictDataException("Событие должно начинаться не позднее, чем через час после публикации. " +
                    "Начало данного события " + event.getEventDate());
        }
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto rejectEvent(Long eventId) {
        Event event = findEventById(eventId);
        if(event.getState().equals(State.PUBLISHED)){
            throw new ConflictDataException(
                    "Событие не должно находиться в статусе PUBLISHED, статус данного события" + event.getState());
        }
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public List<Event> getEventsListById(List<Long> eventsId) {
        return eventRepository.getEventsListById(eventsId);
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено", eventId)));
    }



}
