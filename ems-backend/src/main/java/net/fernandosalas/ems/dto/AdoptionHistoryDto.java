package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionHistoryDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long petId;
    private String petName;
    private String category;
    private LocalDateTime adoptedAt;
    private LocalDateTime returnedAt;
}
