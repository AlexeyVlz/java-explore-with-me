package explore.with.me.controllers.publicControllers;

import explore.with.me.models.user.UserDto;
import explore.with.me.services.publicServices.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class PublicUserController {

    private final PublicUserService publicUserService;

    @GetMapping
    public List<UserDto> getUserRating(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.format("Получен эндпонит GET /users; from = %d, size = %d", from, size));
        List<UserDto> users = publicUserService.getUserRating(from, size);
        log.info("Возвращен список пользователей: " + users);
        return users;
    }
}
