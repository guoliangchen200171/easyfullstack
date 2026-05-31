package net.fernandosalas.ems.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.kafka.MembershipPurchasePointsProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
@Slf4j
public class PurchaseCompletedEventListener {

    private final MembershipPurchasePointsProducer membershipPurchasePointsProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPurchaseCompleted(PurchaseCompletedEvent event) {
        if (event.getUserId() == null) {
            return;
        }
        membershipPurchasePointsProducer.publishPurchasePoints(
                event.getUserId(),
                event.getTotalCost());
    }
}
