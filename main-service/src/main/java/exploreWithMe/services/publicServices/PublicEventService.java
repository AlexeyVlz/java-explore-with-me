package exploreWithMe.services.publicServices;

import exploreWithMe.client.EventsClient;
import exploreWithMe.controllers.publicControllers.PublicEventRestrictions;
import exploreWithMe.exeption.DataNotFound;
import exploreWithMe.models.State;
import exploreWithMe.models.event.Event;
import exploreWithMe.models.event.EventFullDto;
import exploreWithMe.models.event.EventMapper;
import exploreWithMe.models.statistic.Hit;
import exploreWithMe.repositories.eventRepositories.EventRepository;
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
        PageRequest pageRequest = PageRequest.of(restrictions.getFrom() / restrictions.getSize(),
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
