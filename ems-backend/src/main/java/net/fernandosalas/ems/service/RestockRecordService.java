package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.RestockRecordDto;

public interface RestockRecordService {

    void recordRestock(Long productId, String productName, int quantity, String operationType);

    PageResponse<RestockRecordDto> getRestockRecordsPage(int page, int size);
}
