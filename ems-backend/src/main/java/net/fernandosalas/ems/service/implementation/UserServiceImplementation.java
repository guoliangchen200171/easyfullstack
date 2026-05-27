package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
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

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isBlank()
                || newPassword == null || newPassword.isBlank()) {
            throw new InvalidSearchParameterException("原密码和新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new InvalidSearchParameterException("新密码长度不能少于 6 位");
        }
        if (currentPassword.equals(newPassword)) {
            throw new InvalidSearchParameterException("新密码不能与原密码相同");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found with id: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidSearchParameterException("原密码不正确");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
