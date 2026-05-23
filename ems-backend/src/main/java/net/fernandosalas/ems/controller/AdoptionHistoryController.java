package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/adoption-history")
@AllArgsConstructor
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

    @GetMapping
    public ResponseEntity<List<AdoptionHistoryDto>> getAllHistory() {
        return new ResponseEntity<>(adoptionHistoryService.getAllHistory(), HttpStatus.OK);
    }

    @GetMapping("student/{studentId}")
    public ResponseEntity<List<AdoptionHistoryDto>> getHistoryByStudentId(
            @PathVariable("studentId") Long studentId) {
        return new ResponseEntity<>(adoptionHistoryService.getHistoryByStudentId(studentId), HttpStatus.OK);
    }
}
