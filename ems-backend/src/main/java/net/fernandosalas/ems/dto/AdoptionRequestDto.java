package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequestDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long petId;
    private String petName;
    private AdoptionRequestStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;
}
