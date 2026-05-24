package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.entity.AdoptionHistory;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.mapper.AdoptionHistoryMapper;
import net.fernandosalas.ems.repository.AdoptionHistoryRepository;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdoptionHistoryServiceImplementation implements AdoptionHistoryService {

    @Autowired
    private AdoptionHistoryRepository adoptionHistoryRepository;

    @Override
    public void recordAdoption(Student student, Pet pet) {
        AdoptionHistory history = new AdoptionHistory();
        history.setStudent(student);
        history.setPet(pet);
        history.setAdoptedAt(LocalDateTime.now());
        adoptionHistoryRepository.save(history);
    }

    @Override
    public void recordReturn(Long studentId) {
        adoptionHistoryRepository.findFirstByStudentIdAndReturnedAtIsNullOrderByAdoptedAtDesc(studentId)
                .ifPresent(history -> {
                    history.setReturnedAt(LocalDateTime.now());
                    adoptionHistoryRepository.save(history);
                });
    }

    @Override
    public List<AdoptionHistoryDto> getAllHistory() {
        return adoptionHistoryRepository.findAllWithDetailsOrderByAdoptedAtDesc()
                .stream()
                .map(AdoptionHistoryMapper::mapToAdoptionHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AdoptionHistoryDto> getHistoryPage(int page, int size) {
        Page<AdoptionHistory> historyPage = adoptionHistoryRepository.findAllByOrderByAdoptedAtDesc(
                PageRequest.of(page, size));
        List<AdoptionHistoryDto> content = historyPage.getContent().stream()
                .map(AdoptionHistoryMapper::mapToAdoptionHistoryDto)
                .collect(Collectors.toList());
        return PageResponse.from(historyPage, content);
    }

    @Override
    public List<AdoptionHistoryDto> getHistoryByStudentId(Long studentId) {
        return adoptionHistoryRepository.findByStudentIdWithDetailsOrderByAdoptedAtDesc(studentId)
                .stream()
                .map(AdoptionHistoryMapper::mapToAdoptionHistoryDto)
                .collect(Collectors.toList());
    }
}
