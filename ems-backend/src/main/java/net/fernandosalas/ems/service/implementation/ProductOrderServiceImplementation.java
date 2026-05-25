package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.entity.ProductOrder;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.mapper.ProductOrderMapper;
import net.fernandosalas.ems.repository.ProductOrderRepository;
import net.fernandosalas.ems.service.ProductOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductOrderServiceImplementation implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    @Override
    public void recordOrder(Student student, Product product, int quantity,
                            BigDecimal unitPrice, BigDecimal totalPrice) {
        ProductOrder order = new ProductOrder();
        order.setStudent(student);
        order.setProduct(product);
        order.setProductName(product.getName());
        order.setQuantity(quantity);
        order.setUnitPrice(unitPrice);
        order.setTotalPrice(totalPrice);
        order.setOrderedAt(LocalDateTime.now());
        productOrderRepository.save(order);
    }

    @Override
    public PageResponse<ProductOrderDto> getOrdersPage(int page, int size) {
        Page<ProductOrder> orderPage = productOrderRepository.findAllByOrderByOrderedAtDesc(
                PageRequest.of(page, size));
        List<ProductOrderDto> content = orderPage.getContent().stream()
                .map(ProductOrderMapper::mapToProductOrderDto)
                .collect(Collectors.toList());
        return PageResponse.from(orderPage, content);
    }

    @Override
    public PageResponse<ProductOrderDto> getOrdersPageByStudentId(
            Long studentId, int page, int size, boolean ascending) {
        Sort sort = ascending
                ? Sort.by("orderedAt").ascending()
                : Sort.by("orderedAt").descending();
        Page<ProductOrder> orderPage = productOrderRepository.findByStudentId(
                studentId, PageRequest.of(page, size, sort));
        List<ProductOrderDto> content = orderPage.getContent().stream()
                .map(ProductOrderMapper::mapToProductOrderDto)
                .collect(Collectors.toList());
        return PageResponse.from(orderPage, content);
    }
}
