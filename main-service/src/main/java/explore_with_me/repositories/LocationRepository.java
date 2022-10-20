package explore_with_me.repositories;

import explore_with_me.models.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {


}
