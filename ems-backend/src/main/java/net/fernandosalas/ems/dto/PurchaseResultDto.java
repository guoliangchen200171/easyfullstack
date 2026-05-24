package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResultDto {
    private Long productId;
    private int quantity;
    private BigDecimal totalCost;
    private BigDecimal remainingDeposit;
    private int remainingStock;
}
