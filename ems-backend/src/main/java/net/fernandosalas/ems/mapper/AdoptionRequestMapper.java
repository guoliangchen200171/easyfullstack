package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.entity.AdoptionRequest;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;

public class AdoptionRequestMapper {

    private AdoptionRequestMapper() {
    }

    public static AdoptionRequestDto mapToDto(AdoptionRequest request) {
        Student student = request.getStudent();
        Pet pet = request.getPet();
        String studentName = student.getFirstName() + " " + student.getLastName();
        return new AdoptionRequestDto(
                request.getId(),
                student.getId(),
                studentName.trim(),
                student.getEmail(),
                pet.getId(),
                pet.getName(),
                request.getStatus(),
                request.getRequestedAt(),
                request.getReviewedAt());
    }
}
