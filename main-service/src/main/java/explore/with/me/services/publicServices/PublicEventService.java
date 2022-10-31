package explore.with.me.services.publicServices;

import explore.with.me.client.EventsClient;
import explore.with.me.controllers.publicControllers.PublicEventRestrictions;
import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.event.Event;
import explore.with.me.models.event.EventMapper;
import explore.with.me.models.event.EventShortDto;
import explore.with.me.models.statistic.Hit;
import explore.with.me.models.State;
import explore.with.me.models.event.EventFullDto;
import explore.with.me.models.statistic.Statistic;
import explore.with.me.repositories.eventRepositories.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        Event event = findEventById(id);
        event = addViews(event, List.of(request.getRequestURI()));
        return EventMapper.toEventFullDto(event);
    }


    public List<EventShortDto> getFilteredEvents(PublicEventRestrictions restrictions, HttpServletRequest request) {
        addStat(request);
        PageRequest pageRequest = PageRequest.of(restrictions.getFrom() / restrictions.getSize(),
                restrictions.getSize());
        return eventRepository.findEventsByParam(restrictions, pageRequest)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public List<Event> getEventsByCategoryId(Long categoryId) {
        return eventRepository.findAllByCategoryId(categoryId);
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено или оно ещё не опубликовано", eventId)));
    }

    private void addStat(HttpServletRequest request) {
        Hit hit = Hit.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        eventsClient.saveStat(hit);
    }

    private Event addViews(Event event, List<String> uris) {
        ResponseEntity<Statistic[]> responseEntity = eventsClient.getStat(uris);
        Statistic[] stat = responseEntity.getBody();
        if (stat.length < 1) {
            event.setViews(0);
            return event;
        }
        event.setViews(stat[0].getHits());
        return event;
    }
}
