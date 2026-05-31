package net.fernandosalas.ems.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.kafka.dto.PurchasePointsMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipPurchasePointsProducer {

    private final KafkaTemplate<String, PurchasePointsMessage> kafkaTemplate;

    @Value("${app.kafka.topic.membership-purchase-points}")
    private String topic;

    public void publishPurchasePoints(Long userId, BigDecimal totalCost) {
        if (userId == null || totalCost == null || totalCost.signum() <= 0) {
            return;
        }
        PurchasePointsMessage message = new PurchasePointsMessage(userId, totalCost);
        String key = String.valueOf(userId);
        kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error(
                                "Failed to publish purchase points message: userId={}, totalCost={}",
                                userId,
                                totalCost,
                                ex);
                    } else {
                        log.info(
                                "Published purchase points message: userId={}, totalCost={}, offset={}",
                                userId,
                                totalCost,
                                result != null && result.getRecordMetadata() != null
                                        ? result.getRecordMetadata().offset()
                                        : null);
                    }
                });
    }
}
