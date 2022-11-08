package explore.with.me.services.publicServices;

import explore.with.me.models.user.User;
import explore.with.me.models.user.UserDto;
import explore.with.me.models.user.UserMapper;
import explore.with.me.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicUserService {

    private final UserRepository userRepository;
    public List<UserDto> getUserRating(Integer from, Integer size) {
        List<User> users = userRepository.findUserRating();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
