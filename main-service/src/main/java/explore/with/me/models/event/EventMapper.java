package explore.with.me.models.event;

import explore.with.me.UtilClass;
import explore.with.me.models.State;
import explore.with.me.models.category.Category;
import explore.with.me.models.category.CategoryMapper;
import explore.with.me.models.user.User;
import explore.with.me.models.user.UserMapper;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), UtilClass.getFormat());
        return new Event(newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                category,
                0,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                eventDate,
                initiator,
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                State.PENDING,
                0);
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(UtilClass.getFormat()),
                event.getDescription(),
                event.getEventDate().format(UtilClass.getFormat()),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews());
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(UtilClass.getFormat()));
        }
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getDescription(),
                event.getEventDate().format(UtilClass.getFormat()),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }
}
