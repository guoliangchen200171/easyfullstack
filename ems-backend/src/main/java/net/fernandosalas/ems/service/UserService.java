package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.enums.Role;

public interface UserService {
    User createUser(String username, String rawPassword, Role role);

    User createStudentUser(String email);

    boolean existsByUsername(String username);

    void deleteById(Long userId);
}
