package explore.with.me;

import explore.with.me.model.EndpointHit;
import explore.with.me.model.ViewStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
public class Controller {

    private final StatService service;

    @Autowired
    public Controller(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    public void hit(@RequestBody EndpointHit endpointHit) {
        log.info("Получен запрос к эндроинт POST /hit; endpointHit = " + endpointHit);
        service.hit(endpointHit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(@RequestParam @NotBlank String start,
                                   @RequestParam @NotBlank String end,
                                   @RequestParam @NotNull  List<String> uris,
                                   @RequestParam Boolean unique) {
        log.info(String.format("Получен запрос к эндроинт GET /stats; start = %s, end = %s, uris = %s, unique = %s",
                start, end, uris, unique));
        List<ViewStats> views= service.getStat(start, end, uris, unique);
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

}
