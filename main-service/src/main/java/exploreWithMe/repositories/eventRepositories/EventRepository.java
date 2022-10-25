package exploreWithMe.repositories.eventRepositories;


import exploreWithMe.models.State;
import exploreWithMe.models.event.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, CustomEventRepository {

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

    Optional<Event> findByIdAndState(Long id, State state);

}
