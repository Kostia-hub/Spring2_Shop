package lesson.repositories;

import lesson.entities.Role;
import lesson.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    List<User> findAllByRoles(Role role);
}