package explore_with_me.services;

import explore_with_me.exeption.DataNotFound;
import explore_with_me.models.user.NewUserRequest;
import explore_with_me.models.user.User;
import explore_with_me.models.user.UserDto;
import explore_with_me.models.user.UserMapper;
import explore_with_me.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if(ids == null) {
            int page = from / size;
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name"));
            return userRepository.findAll(pageRequest).
                    stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids).
                    stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
    }

    public UserDto addUser(NewUserRequest newUserRequest) {
        User user =  userRepository.save(UserMapper.toUser(newUserRequest, null));
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
