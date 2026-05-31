package net.fernandosalas.membership.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.membership.kafka.dto.PurchasePointsMessage;
import net.fernandosalas.membership.service.MembershipService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchasePointsKafkaListener {

    private final MembershipService membershipService;

    @KafkaListener(
            topics = "${app.kafka.topic.membership-purchase-points}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "purchasePointsKafkaListenerContainerFactory")
    public void onPurchasePoints(PurchasePointsMessage message) {
        if (message == null || message.getUserId() == null) {
            log.warn("Ignoring empty purchase points message");
            return;
        }
        BigDecimal totalCost = message.getTotalCost();
        log.info(
                "Consuming purchase points message: userId={}, totalCost={}",
                message.getUserId(),
                totalCost);
        if (totalCost == null || totalCost.signum() <= 0) {
            log.warn(
                    "Skipping purchase points: userId={}, totalCost={} (must be > 0)",
                    message.getUserId(),
                    totalCost);
            return;
        }
        membershipService.addPointsForPurchase(message.getUserId(), totalCost);
    }
}
