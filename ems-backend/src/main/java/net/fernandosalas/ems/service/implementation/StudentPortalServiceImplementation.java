package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductDetailCacheDto;
import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.ProductInventoryRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.security.SecurityUtils;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import net.fernandosalas.ems.service.AdoptionRequestService;
import net.fernandosalas.ems.service.ProductDetailCacheService;
import net.fernandosalas.ems.service.ProductOrderService;
import net.fernandosalas.ems.service.ProductService;
import net.fernandosalas.ems.service.ProductStockCacheService;
import net.fernandosalas.ems.service.StudentPortalService;
import net.fernandosalas.ems.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentPortalServiceImplementation implements StudentPortalService {

    private static final long INSUFFICIENT_STOCK = -1L;

    private final StudentRepository studentRepository;
    private final ProductInventoryRepository inventoryRepository;
    private final ProductDetailCacheService detailCacheService;
    private final ProductStockCacheService stockCacheService;
    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final AdoptionRequestService adoptionRequestService;
    private final StudentService studentService;
    private final AdoptionHistoryService adoptionHistoryService;

    @Override
    public StudentProfileDto getCurrentStudentProfile() {
        Student student = getCurrentStudentEntity();
        return toProfileDto(student);
    }

    @Override
    public AdoptionRequestDto applyForAdoptionForCurrentStudent(Long petId) {
        return adoptionRequestService.applyForAdoption(requireStudentId(), petId);
    }

    @Override
    public AdoptionRequestDto getCurrentStudentPendingRequest() {
        return adoptionRequestService.getMyPendingRequest(requireStudentId());
    }

    @Override
    public StudentProfileDto returnPetForCurrentStudent() {
        Long studentId = requireStudentId();
        studentService.returnPet(studentId);
        return getCurrentStudentProfile();
    }

    @Override
    public List<AdoptionHistoryDto> getCurrentStudentHistory() {
        return adoptionHistoryService.getHistoryByStudentId(requireStudentId());
    }

    @Override
    public List<ProductDto> listProductsForCurrentStudent() {
        return productService.getAllProducts();
    }

    @Override
    @Transactional
    public PurchaseResultDto purchaseProductForCurrentStudent(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidSearchParameterException("购买数量必须大于 0");
        }
        Long studentId = requireStudentId();

        ProductDetailCacheDto detail = detailCacheService.getByProductId(productId);

        Student student = studentRepository.findByIdForUpdate(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found"));

        BigDecimal price = detail.getPrice() != null ? detail.getPrice() : BigDecimal.ZERO;
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal deposit = student.getDeposit() != null ? student.getDeposit() : BigDecimal.ZERO;

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSearchParameterException("商品价格必须大于 0，请联系管理员设置价格");
        }

        if (deposit.compareTo(totalCost) < 0) {
            throw new InvalidSearchParameterException("存款余额不足");
        }

        Optional<Long> redisDeduct = stockCacheService.deductIfAvailable(productId, quantity);
        if (redisDeduct.isPresent()) {
            if (redisDeduct.get() == INSUFFICIENT_STOCK) {
                throw new InvalidSearchParameterException("库存不足");
            }
            return completePurchaseWithDb(
                    productId, quantity, detail, student, price, totalCost, redisDeduct.get().intValue(), true);
        }

        return purchaseWithDatabaseStockOnly(productId, quantity, detail, student, price, totalCost);
    }

    private PurchaseResultDto completePurchaseWithDb(
            Long productId,
            int quantity,
            ProductDetailCacheDto detail,
            Student student,
            BigDecimal price,
            BigDecimal totalCost,
            int remainingStockFromRedis,
            boolean rollbackRedisOnDbFailure) {
        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));

        int updatedRows = inventoryRepository.deductStockIfAvailable(productId, quantity);
        if (updatedRows == 0) {
            if (rollbackRedisOnDbFailure) {
                stockCacheService.rollback(productId, quantity);
            }
            throw new InvalidSearchParameterException("库存不足");
        }

        BigDecimal deposit = student.getDeposit() != null ? student.getDeposit() : BigDecimal.ZERO;
        student.setDeposit(deposit.subtract(totalCost));
        studentRepository.save(student);
        productOrderService.recordOrder(
                student, inventory, detail.getName(), quantity, price, totalCost);

        int remainingStock = rollbackRedisOnDbFailure
                ? remainingStockFromRedis
                : stockCacheService.getStock(productId);

        return new PurchaseResultDto(
                inventory.getId(),
                quantity,
                totalCost,
                student.getDeposit(),
                remainingStock);
    }

    private PurchaseResultDto purchaseWithDatabaseStockOnly(
            Long productId,
            int quantity,
            ProductDetailCacheDto detail,
            Student student,
            BigDecimal price,
            BigDecimal totalCost) {
        ProductInventory inventory = inventoryRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));

        if (inventory.getStock() < quantity) {
            throw new InvalidSearchParameterException("库存不足");
        }

        return completePurchaseWithDb(
                productId, quantity, detail, student, price, totalCost,
                inventory.getStock() - quantity, false);
    }

    @Override
    public PageResponse<ProductOrderDto> getCurrentStudentProductOrdersPage(
            int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice) {
        return productOrderService.getOrdersPageByStudentId(
                requireStudentId(), page, size, ascending, productName,
                fromDate, toDate, minPrice, maxPrice);
    }

    private Long requireStudentId() {
        UserPrincipal principal = SecurityUtils.getCurrentUser();
        if (principal.getRole() != Role.STUDENT) {
            throw new ResourceNotFoundException("当前用户不是学生账号");
        }
        return studentRepository.findByUserId(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found for current user"))
                .getId();
    }

    private Student getCurrentStudentEntity() {
        return studentRepository.findByIdWithDetails(requireStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found"));
    }

    private StudentProfileDto toProfileDto(Student student) {
        Department department = student.getDepartment();
        Pet pet = student.getPet();
        return new StudentProfileDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                department != null ? department.getId() : null,
                department != null ? department.getDepartmentName() : null,
                pet != null ? pet.getId() : null,
                pet != null ? pet.getName() : null,
                student.getReturnCount(),
                student.getDeposit());
    }
}
