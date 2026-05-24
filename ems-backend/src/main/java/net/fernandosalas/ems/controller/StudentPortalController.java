package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.service.StudentPortalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/me")
@AllArgsConstructor
public class StudentPortalController {

    private final StudentPortalService studentPortalService;

    @GetMapping
    public ResponseEntity<StudentProfileDto> getProfile() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentProfile());
    }

    @PutMapping("/adopt-pet/{petId}")
    public ResponseEntity<Pet> adoptPet(@PathVariable Long petId) {
        Pet adoptedPet = studentPortalService.adoptPetForCurrentStudent(petId);
        return new ResponseEntity<>(adoptedPet, HttpStatus.OK);
    }

    @PutMapping("/return-pet")
    public ResponseEntity<StudentProfileDto> returnPet() {
        return ResponseEntity.ok(studentPortalService.returnPetForCurrentStudent());
    }

    @GetMapping("/history")
    public ResponseEntity<List<AdoptionHistoryDto>> getHistory() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentHistory());
    }
}
