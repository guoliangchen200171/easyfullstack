package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;

public class StudentMapper {
    public static StudentDto mapToStudentDto(Student student) {
        Long departmentId = student.getDepartment() != null ? student.getDepartment().getId() : null;
        Pet pet = student.getPet();
        Long petId = pet != null ? pet.getId() : null;
        String petName = pet != null ? pet.getName() : null;

        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                departmentId,
                petId,
                petName,
                student.getReturnCount()
        );
    }

    public static Student mapToStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        return student;
    }
}
