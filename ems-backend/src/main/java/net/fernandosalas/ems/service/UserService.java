package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.enums.Role;

public interface UserService {
    User createUser(String username, String rawPassword, Role role, Long studentId, Long departmentId);

    void createStudentUser(String email, Long studentId);

    boolean existsByUsername(String username);
}
