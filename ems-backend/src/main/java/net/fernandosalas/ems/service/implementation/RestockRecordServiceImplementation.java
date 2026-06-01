package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.RestockRecordDto;
import net.fernandosalas.ems.entity.RestockRecord;
import net.fernandosalas.ems.repository.RestockRecordRepository;
import net.fernandosalas.ems.service.RestockRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestockRecordServiceImplementation implements RestockRecordService {

    private final RestockRecordRepository restockRecordRepository;

    @Override
    public void recordRestock(Long productId, String productName, int quantity, String operationType) {
        RestockRecord record = new RestockRecord();
        record.setProductId(productId);
        record.setProductName(productName);
        record.setQuantity(quantity);
        record.setOperationType(operationType);
        record.setRestockedAt(LocalDateTime.now());
        restockRecordRepository.save(record);
    }

    @Override
    public PageResponse<RestockRecordDto> getRestockRecordsPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<RestockRecord> recordPage = restockRecordRepository.findAllByOrderByRestockedAtDesc(pageable);
        List<RestockRecordDto> content = recordPage.getContent().stream()
                .map(r -> new RestockRecordDto(
                        r.getId(),
                        r.getProductId(),
                        r.getProductName(),
                        r.getQuantity(),
                        r.getOperationType(),
                        r.getRestockedAt()))
                .collect(Collectors.toList());
        return PageResponse.from(recordPage, content);
    }
}
