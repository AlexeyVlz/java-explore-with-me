package explore.with.me.services.adminServices;

import explore.with.me.exeption.DataNotFound;
import explore.with.me.models.user.UserMapper;
import explore.with.me.repositories.UserRepository;
import explore.with.me.models.user.NewUserRequest;
import explore.with.me.models.user.User;
import explore.with.me.models.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            int page = from / size;
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
            return userRepository.findAll(pageRequest)
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids)
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
    }

    public UserDto addUser(NewUserRequest newUserRequest) {
        User user = userRepository.save(UserMapper.toUser(newUserRequest, null));
        return UserMapper.toUserDto(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFound(
                String.format("Юзер с id %d в базе данных не обнаржен", id)));
    }
}
