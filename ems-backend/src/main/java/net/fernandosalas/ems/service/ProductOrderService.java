package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.entity.Student;

import java.math.BigDecimal;

public interface ProductOrderService {

    void recordOrder(Student student, Product product, int quantity,
                     BigDecimal unitPrice, BigDecimal totalPrice);

    PageResponse<ProductOrderDto> getOrdersPage(int page, int size);
}
