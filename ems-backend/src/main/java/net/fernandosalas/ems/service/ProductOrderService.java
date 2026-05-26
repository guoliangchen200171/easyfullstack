package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.entity.Student;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProductOrderService {

    void recordOrder(Student student, ProductInventory product, String productName,
                     int quantity, BigDecimal unitPrice, BigDecimal totalPrice);

    PageResponse<ProductOrderDto> getOrdersPage(
            int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice);

    PageResponse<ProductOrderDto> getOrdersPageByStudentId(
            Long studentId, int page, int size, boolean ascending, String productName,
            LocalDate fromDate, LocalDate toDate, BigDecimal minPrice, BigDecimal maxPrice);
}
