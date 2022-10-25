package exploreWithMe.repositories.eventRepositories;

import exploreWithMe.controllers.adminControllers.AdminEventRestrictions;
import exploreWithMe.controllers.publicControllers.PublicEventRestrictions;
import exploreWithMe.models.event.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomEventRepository {

    List<Event> findEventsByParam(PublicEventRestrictions restrictions, Pageable pageable);

    List<Event> adminGetEvents(AdminEventRestrictions restrictions, Pageable pageable);

    List<Event> getEventsListById(List<Long> eventsId);
}
