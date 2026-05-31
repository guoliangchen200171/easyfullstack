package net.fernandosalas.ems;

import net.fernandosalas.ems.dto.DepartmentRegisterRequest;
import net.fernandosalas.ems.dto.StudentRegisterRequest;
import net.fernandosalas.ems.service.AuthRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class AuthRegistrationIntegrationTest {

    @Autowired
    private AuthRegistrationService authRegistrationService;

    @Test
    @Transactional
    void registerDepartment_doesNotThrow() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        DepartmentRegisterRequest request = new DepartmentRegisterRequest(
                "Dept" + suffix,
                "desc",
                "dept_user_" + suffix,
                "password123");
        assertDoesNotThrow(() -> authRegistrationService.registerDepartment(request));
    }

    @Test
    @Transactional
    void registerStudent_doesNotThrow() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        StudentRegisterRequest request = new StudentRegisterRequest(
                "First",
                "Last",
                "student_" + suffix + "@example.com",
                3L);
        assertDoesNotThrow(() -> authRegistrationService.registerStudent(request));
    }
}
