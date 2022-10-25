package exploreWithMe.models.compilation;

import exploreWithMe.models.event.Event;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "event_compilations",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "compilation_id")}
    )
    private List<Event> events;
    private Boolean pinned;

    public Compilation(String title, List<Event> events, Boolean pinned) {
        this.title = title;
        this.events = events;
        this.pinned = pinned;
    }

    public Compilation() {
    }
}
