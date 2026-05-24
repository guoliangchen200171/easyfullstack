package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.UsernameAlreadyExistsException;
import net.fernandosalas.ems.repository.UserRepository;
import net.fernandosalas.ems.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    public static final String DEFAULT_STUDENT_PASSWORD = "123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(String username, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("该用户名已被注册");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User createStudentUser(String email) {
        return createUser(email, DEFAULT_STUDENT_PASSWORD, Role.STUDENT);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
