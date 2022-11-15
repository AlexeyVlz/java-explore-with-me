package explore.with.me.models.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Statistic {

    private String app;
    private String uri;
    private Integer hits;

    public Statistic() {
    }
}
