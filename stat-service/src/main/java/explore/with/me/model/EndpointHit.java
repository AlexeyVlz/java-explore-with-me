package explore.with.me.model;

import lombok.Data;

@Data
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public EndpointHit(Long id, String app, String uri, String ip, String timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndpointHit() {
    }
}
