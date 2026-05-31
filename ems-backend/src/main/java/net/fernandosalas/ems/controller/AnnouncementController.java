package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AnnouncementDto;
import net.fernandosalas.ems.service.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@AllArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody AnnouncementDto announcement) {
        AnnouncementDto created = announcementService.createAnnouncement(announcement);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDto>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AnnouncementDto>> getActiveAnnouncements() {
        return ResponseEntity.ok(announcementService.getActiveAnnouncements());
    }

    @GetMapping("{id}")
    public ResponseEntity<AnnouncementDto> getAnnouncementById(@PathVariable("id") Long announcementId) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(announcementId));
    }

    @PutMapping("{id}")
    public ResponseEntity<AnnouncementDto> updateAnnouncement(
            @PathVariable("id") Long announcementId,
            @RequestBody AnnouncementDto announcement) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(announcementId, announcement));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable("id") Long announcementId) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok("Delete Announcement Successfully");
    }
}
