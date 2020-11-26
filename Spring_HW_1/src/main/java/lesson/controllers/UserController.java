package lesson.controllers;

import lesson.controllers.dto.UserDto;
import lesson.controllers.dto.RoleDto;
import lesson.entities.User;
import lesson.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(value = "type", required = false) RoleDto type) {
        return userService.getAllUsersWithType(type);
    }

}