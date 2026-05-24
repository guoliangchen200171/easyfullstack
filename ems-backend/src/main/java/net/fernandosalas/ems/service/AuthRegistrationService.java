package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.DepartmentRegisterRequest;
import net.fernandosalas.ems.dto.StudentRegisterRequest;

public interface AuthRegistrationService {
    void registerDepartment(DepartmentRegisterRequest request);

    void registerStudent(StudentRegisterRequest request);
}
