package net.fernandosalas.membership.kafka;

import net.fernandosalas.membership.kafka.dto.PurchasePointsMessage;
import net.fernandosalas.membership.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PurchasePointsKafkaListenerTest {

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private PurchasePointsKafkaListener listener;

    @Test
    void onPurchasePoints_callsMembershipService() {
        PurchasePointsMessage message = new PurchasePointsMessage(42L, new BigDecimal("12.50"));
        listener.onPurchasePoints(message);
        verify(membershipService).addPointsForPurchase(42L, new BigDecimal("12.50"));
    }

    @Test
    void onPurchasePoints_ignoresNullUserId() {
        listener.onPurchasePoints(new PurchasePointsMessage(null, BigDecimal.ONE));
        verifyNoInteractions(membershipService);
    }

    @Test
    void onPurchasePoints_skipsZeroTotalCost() {
        listener.onPurchasePoints(new PurchasePointsMessage(42L, BigDecimal.ZERO));
        verifyNoInteractions(membershipService);
    }
}
