package explore.with.me.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "statistics")
public class EndpointHit {

    @Id
    @Column(name = "statistic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @Column(name = "timestamp_statistic")
    private LocalDateTime timestamp;

}
