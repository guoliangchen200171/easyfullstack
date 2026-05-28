package net.fernandosalas.membership.service;

import net.fernandosalas.membership.dto.MembershipLevelDto;

import java.util.List;

public interface MembershipLevelService {

    List<MembershipLevelDto> findAll();

    MembershipLevelDto findById(Long id);

    MembershipLevelDto create(MembershipLevelDto dto);

    MembershipLevelDto update(Long id, MembershipLevelDto dto);

    void delete(Long id);

    String resolveLevelCode(long points);

    String resolveLevelName(String levelCode);

    void recalculateAllMemberLevels();
}
