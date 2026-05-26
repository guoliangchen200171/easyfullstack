package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.entity.ProductOrder;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.mapper.ProductOrderMapper;
import net.fernandosalas.ems.repository.ProductOrderRepository;
import net.fernandosalas.ems.service.ProductOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductOrderServiceImplementation implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    @Override
    public void recordOrder(Student student, ProductInventory product, String productName,
                            int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        ProductOrder order = new ProductOrder();
        order.setStudent(student);
        order.setProduct(product);
        order.setProductName(productName);
        order.setQuantity(quantity);
        order.setUnitPrice(unitPrice);
        order.setTotalPrice(totalPrice);
        order.setOrderedAt(LocalDateTime.now());
        productOrderRepository.save(order);
    }

    @Override
    public PageResponse<ProductOrderDto> getOrdersPage(
            int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice) {
        validateDateRange(fromDate, toDate);
        validatePriceRange(minPrice, maxPrice);

        String nameFilter = productName == null || productName.isBlank() ? null : productName.trim();
        LocalDateTime fromAt = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toAt = toDate != null ? toDate.atTime(LocalTime.of(23, 59, 59)) : null;

        Sort sort = ascending
                ? Sort.by("orderedAt").ascending()
                : Sort.by("orderedAt").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<ProductOrder> orderPage = productOrderRepository.searchAllWithFilters(
                nameFilter, fromAt, toAt, minPrice, maxPrice, pageable);
        List<ProductOrderDto> content = orderPage.getContent().stream()
                .map(ProductOrderMapper::mapToProductOrderDto)
                .collect(Collectors.toList());
        return PageResponse.from(orderPage, content);
    }

    @Override
    public PageResponse<ProductOrderDto> getOrdersPageByStudentId(
            Long studentId, int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice) {
        validateDateRange(fromDate, toDate);
        validatePriceRange(minPrice, maxPrice);

        String nameFilter = productName == null || productName.isBlank() ? null : productName.trim();
        LocalDateTime fromAt = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toAt = toDate != null ? toDate.atTime(LocalTime.of(23, 59, 59)) : null;

        Sort sort = ascending
                ? Sort.by("orderedAt").ascending()
                : Sort.by("orderedAt").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<ProductOrder> orderPage = productOrderRepository.searchByStudentWithFilters(
                studentId, nameFilter, fromAt, toAt, minPrice, maxPrice, pageable);
        List<ProductOrderDto> content = orderPage.getContent().stream()
                .map(ProductOrderMapper::mapToProductOrderDto)
                .collect(Collectors.toList());
        return PageResponse.from(orderPage, content);
    }

    private static void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new InvalidSearchParameterException("开始日期不能晚于结束日期");
        }
    }

    private static void validatePriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSearchParameterException("最低总价不能为负数");
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSearchParameterException("最高总价不能为负数");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new InvalidSearchParameterException("最低总价不能高于最高总价");
        }
    }
}
