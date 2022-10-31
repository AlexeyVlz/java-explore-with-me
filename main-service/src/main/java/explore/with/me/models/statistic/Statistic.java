package explore.with.me.models.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Statistic {

    String app;
    String uri;
    Integer hits;

    public Statistic() {
    }
}
