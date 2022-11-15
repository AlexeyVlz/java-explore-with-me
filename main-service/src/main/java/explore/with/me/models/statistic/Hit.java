package explore.with.me.models.statistic;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Hit {


    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

}
