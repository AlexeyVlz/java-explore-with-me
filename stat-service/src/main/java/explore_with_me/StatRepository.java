package explore_with_me;

import explore_with_me.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Modifying
    @Query("select count(e) from EndpointHit as e where e.uri in(?1) and e.timestamp >= ?2 and e.timestamp <= ?3")
    Long getStatWithoutUnique(String uris, LocalDateTime startTime, LocalDateTime endTime);

    @Modifying
    @Query("select e.uri, count(e) from EndpointHit as e where e.uri in(?1) and e.timestamp >= ?2 and e.timestamp <= ?3 " +
            "group by e.uri")
    Long getStatWithUnique(String uris, LocalDateTime startTime, LocalDateTime endTime);
}
