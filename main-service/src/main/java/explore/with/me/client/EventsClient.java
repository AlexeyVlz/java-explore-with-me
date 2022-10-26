package explore.with.me.client;

import explore.with.me.models.statistic.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class EventsClient extends BaseClient {

    @Autowired
    public EventsClient(@Value("${stat-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addStat(Hit hit) {
        return post("/hit", hit);
    }

    public ResponseEntity<Object> getStat(String start, String end, List<String> uris, Boolean unique) {
        start = URLEncoder.encode(start, StandardCharsets.UTF_8);
        end = URLEncoder.encode(end, StandardCharsets.UTF_8);
        StringBuilder urisString = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                urisString.append("uris").append(i + 1).append("=").append(uris.get(i)).append("&");
            } else {
                urisString.append("uris").append(i + 1).append("=").append(uris.get(i));
            }
        }
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get("/stats?start={start}&end={end}&unique={unique}&" + urisString, parameters);
    }
}