package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
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

    @GetMapping("/adoption-request/pending")
    public ResponseEntity<AdoptionRequestDto> getPendingAdoptionRequest() {
        AdoptionRequestDto pending = studentPortalService.getCurrentStudentPendingRequest();
        if (pending == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/adopt-pet/{petId}")
    public ResponseEntity<AdoptionRequestDto> applyForAdoption(@PathVariable Long petId) {
        AdoptionRequestDto request = studentPortalService.applyForAdoptionForCurrentStudent(petId);
        return new ResponseEntity<>(request, HttpStatus.OK);
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
