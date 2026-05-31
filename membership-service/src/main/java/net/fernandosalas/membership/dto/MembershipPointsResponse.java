package net.fernandosalas.membership.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPointsResponse {
    private Long userId;
    private long points;
    private String memberLevel;
    private String levelName;
    private BigDecimal discountRate;
}
