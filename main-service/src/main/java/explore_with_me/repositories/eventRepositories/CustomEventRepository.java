package explore_with_me.repositories.eventRepositories;

import explore_with_me.controllers.adminControllers.AdminEventRestrictions;
import explore_with_me.controllers.publicControllers.PublicEventRestrictions;
import explore_with_me.models.event.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomEventRepository {

    List<Event> findEventsByParam(PublicEventRestrictions restrictions, Pageable pageable);

    List<Event> adminGetEvents(AdminEventRestrictions restrictions, Pageable pageable);
}
