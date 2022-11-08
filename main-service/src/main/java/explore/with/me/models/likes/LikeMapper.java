package explore.with.me.models.likes;

import explore.with.me.models.event.Event;

public class LikeMapper {

    public static LikeOut toLikeOut(Event event) {
        return new LikeOut(event.getId(), event.getLikeCount(), event.getDislikeCount());
    }
}
