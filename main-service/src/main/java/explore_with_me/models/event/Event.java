package explore_with_me.models.event;

import explore_with_me.models.State;
import explore_with_me.models.category.Category;
import explore_with_me.models.location.Location;
import explore_with_me.models.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(nullable = false)
    private Integer confirmedRequests;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column(nullable = false)
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    @Column(nullable = false)
    private Boolean requestModeration;
    @Column(nullable = false)
    private State state;
    @Column(nullable = false)
    private Integer views;

    public Event(String title, String annotation, Category category, Integer confirmedRequests, LocalDateTime createdOn,
                 String description, LocalDateTime eventDate, User initiator, Location location, Boolean paid,
                 Integer participantLimit, LocalDateTime publishedOn, Boolean requestModeration, State state,
                 Integer views) {
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.views = views;
    }

    public Event() {

    }
}
