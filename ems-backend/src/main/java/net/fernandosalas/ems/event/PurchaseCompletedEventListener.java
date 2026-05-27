package net.fernandosalas.ems.event;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.service.MembershipRemoteService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
@Slf4j
public class PurchaseCompletedEventListener {

    private final MembershipRemoteService membershipRemoteService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPurchaseCompleted(PurchaseCompletedEvent event) {
        if (event.getUserId() == null) {
            return;
        }
        try {
            membershipRemoteService.addPointsForPurchase(event.getUserId(), event.getTotalCost());
        } catch (FeignException ex) {
            log.error(
                    "Membership points not added after purchase commit: userId={}, totalCost={}",
                    event.getUserId(),
                    event.getTotalCost(),
                    ex);
        }
    }
}
