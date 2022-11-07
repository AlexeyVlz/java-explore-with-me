package explore.with.me.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ViewStats {

    String app;
    String uri;
    Long hits;

}
