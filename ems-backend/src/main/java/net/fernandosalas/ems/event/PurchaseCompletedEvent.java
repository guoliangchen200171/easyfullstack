package net.fernandosalas.ems.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class PurchaseCompletedEvent extends ApplicationEvent {

    private final Long userId;
    private final BigDecimal totalCost;

    public PurchaseCompletedEvent(Object source, Long userId, BigDecimal totalCost) {
        super(source);
        this.userId = userId;
        this.totalCost = totalCost;
    }
}
