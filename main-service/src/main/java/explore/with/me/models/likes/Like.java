package explore.with.me.models.likes;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "likes")
public class Like {

    @Id
    @Column(name = "like_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long likeId;
    @Column(name = "liker_id", length = 64, nullable = false)
    Long likerId;
    @Column(name = "event_id", nullable = false)
    Long eventId;
    @Column(name = "is_like", nullable = false)
    Boolean isLike;

    public Like(Long likerId, Long eventId, Boolean isLike) {
        this.likerId = likerId;
        this.eventId = eventId;
        this.isLike = isLike;
    }

    public Like() {
    }
}
