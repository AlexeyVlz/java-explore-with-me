package exploreWithMe.models.location;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "location_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull @Positive
    @Column(nullable = false)
    private Double lat;
    @NotNull @Positive
    @Column(nullable = false)
    private Double lon;
}
