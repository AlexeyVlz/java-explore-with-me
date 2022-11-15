package explore.with.me.models.likes;

import lombok.Getter;

@Getter
public class LikeOut {

    private final Long eventId;
    private final Long likeCount;
    private final Long dislikeCount;

    public LikeOut(Long eventId, Long likeCount, Long dislikeCount) {
        this.eventId = eventId;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
