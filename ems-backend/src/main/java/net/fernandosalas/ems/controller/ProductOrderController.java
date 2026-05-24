package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.service.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-orders")
@AllArgsConstructor
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductOrderDto>> getAllOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "10") int size) {
        if (page == null) {
            return new ResponseEntity<>(productOrderService.getOrdersPage(0, size), HttpStatus.OK);
        }
        return new ResponseEntity<>(productOrderService.getOrdersPage(page, size), HttpStatus.OK);
    }
}
