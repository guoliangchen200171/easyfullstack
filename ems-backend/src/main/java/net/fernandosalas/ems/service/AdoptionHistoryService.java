package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;

import java.util.List;

public interface AdoptionHistoryService {

    void recordAdoption(Student student, Pet pet);

    void recordReturn(Long studentId);

    List<AdoptionHistoryDto> getAllHistory();

    PageResponse<AdoptionHistoryDto> getHistoryPage(int page, int size);

    List<AdoptionHistoryDto> getHistoryByStudentId(Long studentId);
}
