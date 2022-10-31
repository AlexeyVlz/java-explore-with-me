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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        eventsClient.addStat(Hit.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build()
        );
    }

    private Event addViews(Event event, List<String> uris) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        start = URLEncoder.encode(start, StandardCharsets.UTF_8);
        end = URLEncoder.encode(end, StandardCharsets.UTF_8);
        StringBuilder urisBuilder = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                urisBuilder.append("uris").append("=").append(uris.get(i)).append("&");
            } else {
                urisBuilder.append("uris").append("=").append(uris.get(i));
            }
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Statistic[]> responseEntity = restTemplate.exchange(
                "http://localhost:9090/stats?start=" + start + "&end=" + end + "&" + urisBuilder + "&unique=true",
                HttpMethod.GET,
                requestEntity,
                Statistic[].class
        );
        Statistic[] stat = responseEntity.getBody();
        if (stat.length < 1) {
            event.setViews(0);
            return event;
        }
        event.setViews(stat[0].getHits());
        return event;
    }
}
