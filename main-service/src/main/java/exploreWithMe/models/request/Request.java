package exploreWithMe.models.request;

import exploreWithMe.models.State;
import exploreWithMe.models.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(nullable = false)
    private State status;

    public Request(LocalDateTime created, Long eventId, User requester, State status) {
        this.created = created;
        this.eventId = eventId;
        this.requester = requester;
        this.status = status;
    }

    public Request(LocalDateTime created, Long eventId, User requester) {
        this.created = created;
        this.eventId = eventId;
        this.requester = requester;
    }

    public Request() {

    }
}
