package lesson.services;

import lesson.controllers.dto.UserDto;
import lesson.controllers.dto.RoleDto;
import lesson.entities.Role;
import lesson.entities.User;
import lesson.exceptions.ManagerIsEarlierThanNeedException;
import lesson.exceptions.UnknownUserTypeException;
import lesson.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User saveUser(UserDto userDto) {
        if (userDto.getRoleDto().equals(RoleDto.MANAGER)) {
            saveManager(userDto);
        } else if (userDto.getRoleDto().equals(RoleDto.USER)) {
            saveTypicallyUser(userDto);
        }

        throw new UnknownUserTypeException();
    }

    private User saveTypicallyUser(UserDto userDto) {
        User user = createUserFromDto(userDto);

        Role role = roleService.getByName("ROLE_USER");
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    private User saveManager(UserDto userDto) {
        if (userDto.getAge() > 18) {
            User user = createUserFromDto(userDto);

            Role role = roleService.getByName("ROLE_MANAGER");
            user.setRoles(List.of(role));

            return userRepository.save(user);
        }

        throw new ManagerIsEarlierThanNeedException("Пользователь младше восемнадцати лет");
    }

    private User createUserFromDto(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setAge(userDto.getAge());

        return user;
    }

    public List<User> getAllUsersWithType(RoleDto roleDto) {
        Role role;

        if(roleDto == RoleDto.USER) {
            role = roleService.getByName("ROLE_USER");
            return userRepository.findAllByRoles(role);
        } else if (roleDto == RoleDto.MANAGER) {
            role = roleService.getByName("ROLE_MANAGER");
            return userRepository.findAllByRoles(role);
        }

        return userRepository.findAll();
    }
}