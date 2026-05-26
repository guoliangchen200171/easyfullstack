package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.dto.PurchaseQuantityRequest;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.service.StudentPortalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/students/me")
@AllArgsConstructor
@Tag(name = "学生门户", description = "学生：个人信息、领养、商品购买与订单查询")
public class StudentPortalController {

    private final StudentPortalService studentPortalService;

    @Operation(summary = "获取个人资料", description = "需要 STUDENT 角色")
    @GetMapping
    public ResponseEntity<StudentProfileDto> getProfile() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentProfile());
    }

    @Operation(summary = "获取待审批领养申请", description = "需要 STUDENT 角色；无待审批时返回 204")
    @GetMapping("/adoption-request/pending")
    public ResponseEntity<AdoptionRequestDto> getPendingAdoptionRequest() {
        AdoptionRequestDto pending = studentPortalService.getCurrentStudentPendingRequest();
        if (pending == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pending);
    }

    @Operation(summary = "申请领养宠物", description = "需要 STUDENT 角色，提交领养审批申请")
    @PutMapping("/adopt-pet/{petId}")
    public ResponseEntity<AdoptionRequestDto> applyForAdoption(@PathVariable Long petId) {
        AdoptionRequestDto request = studentPortalService.applyForAdoptionForCurrentStudent(petId);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @Operation(summary = "归还宠物", description = "需要 STUDENT 角色")
    @PutMapping("/return-pet")
    public ResponseEntity<StudentProfileDto> returnPet() {
        return ResponseEntity.ok(studentPortalService.returnPetForCurrentStudent());
    }

    @Operation(summary = "获取领养历史", description = "需要 STUDENT 角色")
    @GetMapping("/history")
    public ResponseEntity<List<AdoptionHistoryDto>> getHistory() {
        return ResponseEntity.ok(studentPortalService.getCurrentStudentHistory());
    }

    @Operation(summary = "获取可购买商品列表", description = "需要 STUDENT 角色")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> listProducts() {
        return ResponseEntity.ok(studentPortalService.listProductsForCurrentStudent());
    }

    @Operation(summary = "购买商品", description = "需要 STUDENT 角色，扣减存款与库存并生成订单")
    @PostMapping("/products/{productId}/purchase")
    public ResponseEntity<PurchaseResultDto> purchaseProduct(
            @PathVariable Long productId,
            @RequestBody PurchaseQuantityRequest request) {
        PurchaseResultDto result = studentPortalService.purchaseProductForCurrentStudent(
                productId, request.getQuantity());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "分页查询我的购买记录", description = "需要 STUDENT 角色；支持商品名、下单日期、总价筛选与排序")
    @GetMapping("/product-orders")
    public ResponseEntity<PageResponse<ProductOrderDto>> getMyProductOrders(
            @Parameter(description = "页码，从 0 开始，默认 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序：desc 最新在前，asc 最早在前") @RequestParam(defaultValue = "desc") String sort,
            @Parameter(description = "商品名称模糊搜索") @RequestParam(required = false) String name,
            @Parameter(description = "开始日期（含），格式 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "结束日期（含当天 23:59:59）") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "最低订单总价（元）") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "最高订单总价（元）") @RequestParam(required = false) BigDecimal maxPrice) {
        boolean ascending = parseSortAscending(sort);
        return ResponseEntity.ok(
                studentPortalService.getCurrentStudentProductOrdersPage(
                        page, size, ascending, name, from, to, minPrice, maxPrice));
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
