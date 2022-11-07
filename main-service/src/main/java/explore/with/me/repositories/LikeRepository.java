package explore.with.me.repositories;

import explore.with.me.models.likes.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("select l from Like l " +
            "where l.likerId = ?1 and l.eventId = ?2")
    Like findLike(Long likerId, Long eventId);
}
