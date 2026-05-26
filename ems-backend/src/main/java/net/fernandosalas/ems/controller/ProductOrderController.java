package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.ProductOrderDto;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.service.ProductOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/product-orders")
@AllArgsConstructor
@Tag(name = "下单记录", description = "管理员：订单分页查询，支持名称/时间/价格筛选")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @Operation(summary = "分页查询全部订单", description = "需要 ADMIN 角色；支持商品名、下单日期、总价筛选与排序")
    @GetMapping
    public ResponseEntity<PageResponse<ProductOrderDto>> getAllOrders(
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
        return new ResponseEntity<>(
                productOrderService.getOrdersPage(
                        page, size, ascending, name, from, to, minPrice, maxPrice),
                HttpStatus.OK);
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
