package explore_with_me.controllers.adminControllers;

import explore_with_me.models.State;
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
