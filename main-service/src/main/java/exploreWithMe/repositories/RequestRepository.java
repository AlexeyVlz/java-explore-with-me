package exploreWithMe.repositories;

import exploreWithMe.models.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    @Modifying
    @Query("select r from Request as r where r.requester.id = ?1 and r.eventId = ?2")
    List<Request> findByEventAndRequester(Long requesterId, Long eventId);
}
