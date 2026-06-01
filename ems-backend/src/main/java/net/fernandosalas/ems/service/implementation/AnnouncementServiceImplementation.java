package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AnnouncementDto;
import net.fernandosalas.ems.entity.Announcement;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.mapper.AnnouncementMapper;
import net.fernandosalas.ems.repository.AnnouncementRepository;
import net.fernandosalas.ems.service.AnnouncementHistoryService;
import net.fernandosalas.ems.service.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementServiceImplementation implements AnnouncementService {

    private static final int MAX_ACTIVE_ANNOUNCEMENTS = 3;

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementHistoryService announcementHistoryService;

    @Override
    @Transactional
    public AnnouncementDto createAnnouncement(AnnouncementDto announcement) {
        validateAnnouncement(announcement);
        validateActiveLimit(announcement.isActive(), null);

        Announcement entity = new Announcement();
        entity.setTitle(announcement.getTitle().trim());
        entity.setContent(announcement.getContent().trim());
        entity.setActive(announcement.isActive());

        Announcement saved = announcementRepository.save(entity);
        announcementHistoryService.recordHistory(saved.getId(), saved.getTitle(), "发布");
        return AnnouncementMapper.toDto(saved);
    }

    @Override
    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(AnnouncementMapper::toDto)
                .toList();
    }

    @Override
    public List<AnnouncementDto> getActiveAnnouncements() {
        return announcementRepository.findTop3ByActiveTrueOrderByUpdatedAtDesc().stream()
                .map(AnnouncementMapper::toDto)
                .toList();
    }

    @Override
    public AnnouncementDto getAnnouncementById(Long announcementId) {
        Announcement announcement = findAnnouncement(announcementId);
        return AnnouncementMapper.toDto(announcement);
    }

    @Override
    @Transactional
    public AnnouncementDto updateAnnouncement(Long announcementId, AnnouncementDto announcement) {
        validateAnnouncement(announcement);
        Announcement entity = findAnnouncement(announcementId);
        validateActiveLimit(announcement.isActive(), entity);

        entity.setTitle(announcement.getTitle().trim());
        entity.setContent(announcement.getContent().trim());
        entity.setActive(announcement.isActive());

        Announcement saved = announcementRepository.save(entity);
        announcementHistoryService.recordHistory(saved.getId(), saved.getTitle(), "更改");
        return AnnouncementMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = findAnnouncement(announcementId);
        announcementHistoryService.recordHistory(announcement.getId(), announcement.getTitle(), "删除");
        announcementRepository.delete(announcement);
    }

    private Announcement findAnnouncement(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Announcement was not found with id: " + announcementId));
    }

    private void validateAnnouncement(AnnouncementDto announcement) {
        if (announcement.getTitle() == null || announcement.getTitle().isBlank()) {
            throw new InvalidSearchParameterException("Announcement title cannot be empty");
        }
        if (announcement.getContent() == null || announcement.getContent().isBlank()) {
            throw new InvalidSearchParameterException("Announcement content cannot be empty");
        }
    }

    private void validateActiveLimit(boolean requestedActive, Announcement existing) {
        if (!requestedActive || (existing != null && existing.isActive())) {
            return;
        }
        if (announcementRepository.countByActiveTrue() >= MAX_ACTIVE_ANNOUNCEMENTS) {
            throw new InvalidSearchParameterException("At most 3 announcements can be active at the same time");
        }
    }
}
