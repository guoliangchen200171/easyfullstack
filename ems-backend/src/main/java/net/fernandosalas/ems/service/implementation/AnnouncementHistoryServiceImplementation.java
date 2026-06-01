package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AnnouncementHistoryDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.entity.AnnouncementHistory;
import net.fernandosalas.ems.repository.AnnouncementHistoryRepository;
import net.fernandosalas.ems.service.AnnouncementHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementHistoryServiceImplementation implements AnnouncementHistoryService {

    private final AnnouncementHistoryRepository announcementHistoryRepository;

    @Override
    public void recordHistory(Long announcementId, String title, String operationType) {
        AnnouncementHistory history = new AnnouncementHistory();
        history.setAnnouncementId(announcementId);
        history.setTitle(title);
        history.setOperationType(operationType);
        history.setOperatedAt(LocalDateTime.now());
        announcementHistoryRepository.save(history);
    }

    @Override
    public PageResponse<AnnouncementHistoryDto> getHistoryPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<AnnouncementHistory> historyPage =
                announcementHistoryRepository.findAllByOrderByOperatedAtDesc(pageable);
        List<AnnouncementHistoryDto> content = historyPage.getContent().stream()
                .map(h -> new AnnouncementHistoryDto(
                        h.getId(),
                        h.getAnnouncementId(),
                        h.getTitle(),
                        h.getOperationType(),
                        h.getOperatedAt()))
                .collect(Collectors.toList());
        return PageResponse.from(historyPage, content);
    }
}
