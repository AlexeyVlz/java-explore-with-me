package explore.with.me;

import explore.with.me.model.EndpointHit;
import explore.with.me.model.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Modifying
    @Query("select e.app, e.uri, count(e) from EndpointHit as e where e.uri in(?1) and e.timestamp >= ?2 " +
            "and e.timestamp <= ?3 " +
            "group by e.uri")
    List<ViewStats> getStatWithoutUnique(String uris, LocalDateTime startTime, LocalDateTime endTime);

    @Modifying
    @Query("select e.app, e.uri, count(e) from EndpointHit as e where e.uri in(?1) and e.timestamp >= ?2 " +
            "and e.timestamp <= ?3 " +
            "group by e.uri")
    List<ViewStats> getStatWithUnique(String uris, LocalDateTime startTime, LocalDateTime endTime);
}
