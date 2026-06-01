package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AnnouncementHistoryDto;
import net.fernandosalas.ems.dto.PageResponse;

public interface AnnouncementHistoryService {

    void recordHistory(Long announcementId, String title, String operationType);

    PageResponse<AnnouncementHistoryDto> getHistoryPage(int page, int size);
}
