package explore.with.me;

import explore.with.me.model.EndpointHit;
import explore.with.me.model.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatService {

    private final StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void hit(EndpointHit endpointHit) {
        statRepository.save(endpointHit);
    }

    public ViewStats getStat(String start, String end, List<String> uris, Boolean unique, String requestURI) {
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String urisString = String.join(",", uris);
        Long count;
        if (unique == null || !unique) {
            count = statRepository.getStatWithoutUnique(urisString, startTime, endTime);
        } else {
            count = statRepository.getStatWithUnique(urisString, startTime, endTime);
        }
        return new ViewStats("stat-service", requestURI, count);
    }
}
