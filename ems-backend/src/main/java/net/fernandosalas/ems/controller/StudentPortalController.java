package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.dto.PurchaseQuantityRequest;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.service.StudentPortalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/me")
@AllArgsConstructor
public class StudentPortalController {

    private final StudentPortalService studentPortalService;

    @GetMapping
    public ResponseEntity<StudentProfileDto> getProfile() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentProfile());
    }

    @GetMapping("/adoption-request/pending")
    public ResponseEntity<AdoptionRequestDto> getPendingAdoptionRequest() {
        AdoptionRequestDto pending = studentPortalService.getCurrentStudentPendingRequest();
        if (pending == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/adopt-pet/{petId}")
    public ResponseEntity<AdoptionRequestDto> applyForAdoption(@PathVariable Long petId) {
        AdoptionRequestDto request = studentPortalService.applyForAdoptionForCurrentStudent(petId);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/return-pet")
    public ResponseEntity<StudentProfileDto> returnPet() {
        return ResponseEntity.ok(studentPortalService.returnPetForCurrentStudent());
    }

    @GetMapping("/history")
    public ResponseEntity<List<AdoptionHistoryDto>> getHistory() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentHistory());
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(studentPortalService.listProductsForCurrentStudent());
    }

    @PostMapping("/products/{productId}/purchase")
    public ResponseEntity<PurchaseResultDto> purchaseProduct(
            @PathVariable Long productId,
            @RequestBody PurchaseQuantityRequest request) {
        PurchaseResultDto result = studentPortalService.purchaseProductForCurrentStudent(
                productId, request.getQuantity());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/product-orders")
    public ResponseEntity<PageResponse<ProductOrderDto>> getMyProductOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort) {
        boolean ascending = parseSortAscending(sort);
        return ResponseEntity.ok(
                studentPortalService.getCurrentStudentProductOrdersPage(page, size, ascending));
    }

    private static boolean parseSortAscending(String sort) {
        if ("asc".equalsIgnoreCase(sort)) {
            return true;
        }
        if ("desc".equalsIgnoreCase(sort)) {
            return false;
        }
        throw new InvalidSearchParameterException("sort 参数仅支持 asc 或 desc");
    }
}
