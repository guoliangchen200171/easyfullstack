package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Product;

import java.util.List;

public interface StudentPortalService {
    StudentProfileDto getCurrentStudentProfile();

    AdoptionRequestDto applyForAdoptionForCurrentStudent(Long petId);

    AdoptionRequestDto getCurrentStudentPendingRequest();

    StudentProfileDto returnPetForCurrentStudent();

    List<AdoptionHistoryDto> getCurrentStudentHistory();

    List<Product> listProductsForCurrentStudent();

    PurchaseResultDto purchaseProductForCurrentStudent(Long productId, int quantity);
}
