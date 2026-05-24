package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.DepartmentStudentPortalDto;

import java.util.List;

public interface DepartmentPortalService {
    DepartmentDto getCurrentDepartment();

    List<DepartmentStudentPortalDto> getCurrentDepartmentStudents();
}
