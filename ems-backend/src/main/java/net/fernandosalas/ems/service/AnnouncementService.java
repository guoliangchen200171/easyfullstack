package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AnnouncementDto;

import java.util.List;

public interface AnnouncementService {

    AnnouncementDto createAnnouncement(AnnouncementDto announcement);

    List<AnnouncementDto> getAllAnnouncements();

    List<AnnouncementDto> getActiveAnnouncements();

    AnnouncementDto getAnnouncementById(Long announcementId);

    AnnouncementDto updateAnnouncement(Long announcementId, AnnouncementDto announcement);

    void deleteAnnouncement(Long announcementId);
}
