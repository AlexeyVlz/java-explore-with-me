package explore_with_me.services.publicServices;

import explore_with_me.client.EventsClient;
import explore_with_me.controllers.publicControllers.PublicEventRestrictions;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.State;
import explore_with_me.models.event.Event;
import explore_with_me.models.event.EventFullDto;
import explore_with_me.models.event.EventMapper;
import explore_with_me.models.statistic.Hit;
import explore_with_me.repositories.eventRepositories.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicEventService {

    private final EventRepository eventRepository;
    private final EventsClient eventsClient;

    public PublicEventService(EventRepository eventRepository, EventsClient eventsClient) {
        this.eventRepository = eventRepository;
        this.eventsClient = eventsClient;
    }

    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        addStat(request);
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено или оно ещё не опубликовано", id)));
        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getFilteredEvents(PublicEventRestrictions restrictions, HttpServletRequest request) {
        addStat(request);
        PageRequest pageRequest = PageRequest.of(restrictions.getFrom()/ restrictions.getSize(),
                                                    restrictions.getSize());
        return eventRepository.findEventsByParam(restrictions, pageRequest)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public List<Event> getEventsByCategoryId(Long categoryId) {
        return eventRepository.findAllByCategoryId(categoryId);
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено или оно ещё не опубликовано", eventId)));
    }

    private void addStat(HttpServletRequest request) {
        eventsClient.addStat(Hit.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build()
        );
    }
}
