package explore.with.me.repositories.eventRepositories;

import explore.with.me.controllers.publicControllers.PublicEventRestrictions;
import explore.with.me.models.event.Event;
import explore.with.me.controllers.adminControllers.AdminEventRestrictions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<Event> findEventsByParam(PublicEventRestrictions restrictions, Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if(restrictions.getText() != null) {
            predicates.add(cb.or(
                    cb.like(cb.lower(eventRoot.get("annotation")), "%" + restrictions.getText().toLowerCase() + "%"),
                    cb.like(cb.lower(eventRoot.get("description")), "%" + restrictions.getText().toLowerCase() + "%")
                    )
            );
        }
        if(restrictions.getCategories() != null && !restrictions.getCategories().isEmpty()) {
            predicates.add(eventRoot.get("category").get("id").in(restrictions.getCategories()));
        }
        if(restrictions.getPaid() != null) {
            predicates.add(cb.equal(eventRoot.get("paid"), restrictions.getPaid()));
        }
        if(restrictions.getRangeStart() != null && restrictions.getRangeEnd() != null) {
            predicates.add(cb.between(eventRoot.get("eventDate"),
                    restrictions.getRangeStart(), restrictions.getRangeEnd()));
        } else {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("eventDate"), LocalDateTime.now()));
        }
        if(restrictions.getOnlyAvailable()) {
            predicates.add(cb.greaterThan(eventRoot.get("participantLimit"), eventRoot.get("confirmedRequests")));
        }
        if(restrictions.getSort() != null) {
            switch (restrictions.getSort()) {
                case("EVENT_DATE"):
                    query.orderBy(cb.desc(eventRoot.get("eventDate")));
                    break;
                case("VIEWS"):
                    query.orderBy(cb.desc(eventRoot.get("views")));
            }
        }
        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }

    public List<Event> adminGetEvents(AdminEventRestrictions restrictions, Pageable pageable){
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if(restrictions.getUsers() != null && !restrictions.getUsers().isEmpty()){
            predicates.add(eventRoot.get("initiator").get("id").in(restrictions.getUsers()));
        }
        if(restrictions.getStates() != null && !restrictions.getStates().isEmpty()){
            predicates.add(eventRoot.get("state").in(restrictions.getStates()));
        }
        if(restrictions.getCategories() != null && !restrictions.getCategories().isEmpty()) {
            predicates.add(eventRoot.get("category").get("id").in(restrictions.getCategories()));
        }
        if(restrictions.getRangeStart() != null && restrictions.getRangeEnd() != null) {
            predicates.add(cb.between(eventRoot.get("eventDate"),
                    restrictions.getRangeStart(), restrictions.getRangeEnd()));
        } else {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("eventDate"), LocalDateTime.now()));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }

    public List<Event> getEventsListById(List<Long> eventsId) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if(eventsId != null && !eventsId.isEmpty()) {
            predicates.add(eventRoot.get("id").in(eventsId));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }

}
