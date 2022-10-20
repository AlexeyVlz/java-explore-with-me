package explore_with_me.services.publicServices;

import explore_with_me.controllers.publicControllers.PublicEventRestrictions;
import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.State;
import explore_with_me.models.event.Event;
import explore_with_me.models.event.EventFullDto;
import explore_with_me.models.event.EventMapper;
import explore_with_me.repositories.eventRepositories.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicEventService {

    private final EventRepository eventRepository;

    public PublicEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventFullDto getEventById(Long id) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED).orElseThrow(() -> new DataNotFound(
                String.format("Событие с id %d в базе данных не обнаржено или оно ещё не опубликовано", id)));
        /*TODO:информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
           информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики*/
        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getFilteredEvents(PublicEventRestrictions restrictions) {
        //TODO информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики
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
}
