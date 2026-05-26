package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface StudentPortalService {
    StudentProfileDto getCurrentStudentProfile();

    AdoptionRequestDto applyForAdoptionForCurrentStudent(Long petId);

    AdoptionRequestDto getCurrentStudentPendingRequest();

    StudentProfileDto returnPetForCurrentStudent();

    List<AdoptionHistoryDto> getCurrentStudentHistory();

    List<ProductDto> listProductsForCurrentStudent();

    PurchaseResultDto purchaseProductForCurrentStudent(Long productId, int quantity);

    PageResponse<ProductOrderDto> getCurrentStudentProductOrdersPage(
            int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice);
}
