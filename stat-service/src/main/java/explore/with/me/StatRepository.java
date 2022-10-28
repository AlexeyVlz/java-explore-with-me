package explore.with.me;

import explore.with.me.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StatRepository extends JpaRepository<Hit, Long> {


    @Query("select count(h) from Hit as h where h.uri in(?1) and h.timestamp >= ?2 " +
            "and h.timestamp <= ?3")
    int getStatWithoutUnique(String uri, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select count(distinct h.ip) from Hit as h where h.uri in(?1) and h.timestamp >= ?2 " +
            "and h.timestamp <= ?3")
    int getStatWithUnique(String uris, LocalDateTime startTime, LocalDateTime endTime);
}
