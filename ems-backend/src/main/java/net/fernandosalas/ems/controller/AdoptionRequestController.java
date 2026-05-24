package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;
import net.fernandosalas.ems.service.AdoptionRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoption-requests")
@AllArgsConstructor
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    @GetMapping
    public ResponseEntity<?> getRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) AdoptionRequestStatus status) {
        return new ResponseEntity<>(
                adoptionRequestService.getRequestsPage(page, size, status), HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<AdoptionRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionRequestService.approve(id));
    }

    @PutMapping("/{id}/deny")
    public ResponseEntity<AdoptionRequestDto> deny(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionRequestService.deny(id));
    }
}
