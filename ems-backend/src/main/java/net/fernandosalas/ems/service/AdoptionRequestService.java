package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;

public interface AdoptionRequestService {

    AdoptionRequestDto applyForAdoption(Long studentId, Long petId);

    PageResponse<AdoptionRequestDto> getRequestsPage(int page, int size, AdoptionRequestStatus status);

    AdoptionRequestDto getMyPendingRequest(Long studentId);

    AdoptionRequestDto approve(Long requestId);

    AdoptionRequestDto deny(Long requestId);
}
