package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.AnnouncementDto;
import net.fernandosalas.ems.entity.Announcement;

public class AnnouncementMapper {

    private AnnouncementMapper() {
    }

    public static AnnouncementDto toDto(Announcement announcement) {
        return new AnnouncementDto(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.isActive(),
                announcement.getCreatedAt(),
                announcement.getUpdatedAt()
        );
    }
}
