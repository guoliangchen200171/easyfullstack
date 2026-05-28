package net.fernandosalas.membership.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipLevelDto {
    private Long id;
    private String levelCode;
    private String levelName;
    private long minPoints;
    private String description;
    private int sortOrder;
}
