package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.entity.ProductOrder;

public class ProductOrderMapper {

    public static ProductOrderDto mapToProductOrderDto(ProductOrder order) {
        String studentName = order.getStudent().getFirstName() + " " + order.getStudent().getLastName();
        return new ProductOrderDto(
                order.getId(),
                order.getStudent().getId(),
                studentName.trim(),
                order.getProduct().getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getOrderedAt()
        );
    }
}
