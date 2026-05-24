package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.DepartmentStudentPortalDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Pet;

import java.util.List;

public interface StudentPortalService {
    StudentProfileDto getCurrentStudentProfile();

    Pet adoptPetForCurrentStudent(Long petId);

    StudentProfileDto returnPetForCurrentStudent();

    List<AdoptionHistoryDto> getCurrentStudentHistory();
}
