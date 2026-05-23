package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.entity.AdoptionHistory;

public class AdoptionHistoryMapper {

    public static AdoptionHistoryDto mapToAdoptionHistoryDto(AdoptionHistory history) {
        String studentName = history.getStudent().getFirstName() + " " + history.getStudent().getLastName();
        return new AdoptionHistoryDto(
                history.getId(),
                history.getStudent().getId(),
                studentName.trim(),
                history.getPet().getId(),
                history.getPet().getName(),
                history.getPet().getCategory().getValue(),
                history.getAdoptedAt(),
                history.getReturnedAt()
        );
    }
}
