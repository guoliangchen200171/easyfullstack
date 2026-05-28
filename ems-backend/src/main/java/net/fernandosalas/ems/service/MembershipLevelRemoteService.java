package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.MembershipLevelDto;

import java.util.List;

public interface MembershipLevelRemoteService {

    List<MembershipLevelDto> findAll();

    MembershipLevelDto findById(Long id);

    MembershipLevelDto create(MembershipLevelDto dto);

    MembershipLevelDto update(Long id, MembershipLevelDto dto);

    void delete(Long id);
}
