package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PurchaseResultDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.ProductRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.security.SecurityUtils;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import net.fernandosalas.ems.service.AdoptionRequestService;
import net.fernandosalas.ems.service.StudentPortalService;
import net.fernandosalas.ems.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentPortalServiceImplementation implements StudentPortalService {

    private final StudentRepository studentRepository;
    private final ProductRepository productRepository;
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
    public List<Product> listProductsForCurrentStudent() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public PurchaseResultDto purchaseProductForCurrentStudent(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidSearchParameterException("购买数量必须大于 0");
        }
        Student student = getCurrentStudentEntity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));

        BigDecimal price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal deposit = student.getDeposit() != null ? student.getDeposit() : BigDecimal.ZERO;

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSearchParameterException("商品价格必须大于 0，请联系管理员设置价格");
        }

        if (deposit.compareTo(totalCost) < 0) {
            throw new InvalidSearchParameterException("存款余额不足");
        }
        if (product.getStock() < quantity) {
            throw new InvalidSearchParameterException("库存不足");
        }

        student.setDeposit(deposit.subtract(totalCost));
        product.setStock(product.getStock() - quantity);
        studentRepository.save(student);
        productRepository.save(product);

        return new PurchaseResultDto(
                product.getId(),
                quantity,
                totalCost,
                student.getDeposit(),
                product.getStock());
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
