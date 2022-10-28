package explore.with.me;

import explore.with.me.model.EndpointHit;
import explore.with.me.model.HitMapper;
import explore.with.me.model.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatService {

    private final StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void hit(EndpointHit endpointHit) {
        statRepository.save(HitMapper.toHit(endpointHit));
    }

    public List<ViewStats> getStat(String start, String end, List<String> uris, Boolean unique) {
        start = URLDecoder.decode(start, StandardCharsets.UTF_8);
        end = URLDecoder.decode(end, StandardCharsets.UTF_8);
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<ViewStats> views = new ArrayList<>();
        for (String uri : uris) {
            if (unique) {
                views.add(new ViewStats("ewm-main-service", uri,
                        statRepository.getStatWithUnique(uri, startTime, endTime)));
            } else {
                views.add(new ViewStats("ewm-main-service", uri,
                        statRepository.getStatWithoutUnique(uri, startTime, endTime)));
            }

        }
        return views;
    }


}
