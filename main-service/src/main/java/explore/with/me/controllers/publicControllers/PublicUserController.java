package explore.with.me.controllers.publicControllers;

import explore.with.me.models.user.UserDto;
import explore.with.me.services.publicServices.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class PublicUserController {

    private final PublicUserService publicUserService;

    @GetMapping
    public List<UserDto> getUserRating() {
        log.info("Получен эндпонит GET /users");
        List<UserDto> users = publicUserService.getUserRating();
        log.info("Возвращен список пользователей: " + users);
        return users;
    }
}
