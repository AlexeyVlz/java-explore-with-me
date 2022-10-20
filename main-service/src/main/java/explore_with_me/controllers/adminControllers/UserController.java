package explore_with_me.controllers.adminControllers;

import explore_with_me.models.user.NewUserRequest;
import explore_with_me.models.user.UserDto;
import explore_with_me.services.adminServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@Slf4j
public class UserController {


    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam (name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam (name = "size", defaultValue = "10") @Positive Integer size){
        log.info(String.format(
                "Получен запрос к эндпоинту: GET: /admin/users;  ids = %s, from = %s, size = %s", ids, from, size));
        if(ids.size() == 0){
            return new ArrayList<>();
        }
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен запрос к эндпоинту: POST: /admin/users; newUserRequest = " + newUserRequest);
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId){
        log.info("Получен запрос к эндпоинту: DELETE: /admin/users/{userId}; userId = " + userId);
        userService.deleteUser(userId);
    }

}
