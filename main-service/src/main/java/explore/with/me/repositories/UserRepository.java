package explore.with.me.repositories;

import explore.with.me.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User as u left join Event as e on u.id = e.initiator.id group by u order by sum(e.likeCount) desc ")
    List<User> findUserRating();
}
