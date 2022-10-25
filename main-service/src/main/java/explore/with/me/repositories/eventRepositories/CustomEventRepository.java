package explore.with.me.repositories.eventRepositories;

import explore.with.me.controllers.publicControllers.PublicEventRestrictions;
import explore.with.me.models.event.Event;
import explore.with.me.controllers.adminControllers.AdminEventRestrictions;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomEventRepository {

    List<Event> findEventsByParam(PublicEventRestrictions restrictions, Pageable pageable);

    List<Event> adminGetEvents(AdminEventRestrictions restrictions, Pageable pageable);

    List<Event> getEventsListById(List<Long> eventsId);
}
