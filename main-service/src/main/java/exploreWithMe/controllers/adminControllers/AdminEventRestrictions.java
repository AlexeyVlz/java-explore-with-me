package exploreWithMe.controllers.adminControllers;

import exploreWithMe.models.State;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class AdminEventRestrictions {

    List<Long> users;
    List<State> states;
    List<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Integer from;
    Integer size;
}
