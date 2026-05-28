package net.fernandosalas.membership.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.dto.MembershipLevelDto;
import net.fernandosalas.membership.entity.Membership;
import net.fernandosalas.membership.entity.MembershipLevel;
import net.fernandosalas.membership.exception.MembershipBadRequestException;
import net.fernandosalas.membership.exception.MembershipNotFoundException;
import net.fernandosalas.membership.repository.MembershipLevelRepository;
import net.fernandosalas.membership.repository.MembershipRepository;
import net.fernandosalas.membership.service.MembershipLevelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MembershipLevelServiceImplementation implements MembershipLevelService {

    private static final String DEFAULT_LEVEL_CODE = "BRONZE";

    private final MembershipLevelRepository membershipLevelRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MembershipLevelDto> findAll() {
        return membershipLevelRepository.findAllByOrderByMinPointsAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipLevelDto findById(Long id) {
        return toDto(getEntity(id));
    }

    @Override
    @Transactional
    public MembershipLevelDto create(MembershipLevelDto dto) {
        validateDto(dto, null);
        MembershipLevel entity = new MembershipLevel();
        applyDto(entity, dto);
        if (entity.getSortOrder() == 0 && dto.getSortOrder() == 0) {
            entity.setSortOrder((int) entity.getMinPoints());
        }
        MembershipLevel saved = membershipLevelRepository.save(entity);
        recalculateAllMemberLevels();
        return toDto(saved);
    }

    @Override
    @Transactional
    public MembershipLevelDto update(Long id, MembershipLevelDto dto) {
        MembershipLevel entity = getEntity(id);
        validateDto(dto, id);
        if (dto.getLevelCode() != null
                && !dto.getLevelCode().isBlank()
                && !dto.getLevelCode().equals(entity.getLevelCode())) {
            if (membershipRepository.countByMemberLevel(entity.getLevelCode()) > 0) {
                throw new MembershipBadRequestException("仍有会员使用该等级代码，无法修改 levelCode");
            }
            entity.setLevelCode(dto.getLevelCode().trim().toUpperCase());
        }
        entity.setLevelName(dto.getLevelName().trim());
        entity.setMinPoints(dto.getMinPoints());
        entity.setDescription(dto.getDescription());
        if (dto.getSortOrder() > 0) {
            entity.setSortOrder(dto.getSortOrder());
        }
        MembershipLevel saved = membershipLevelRepository.save(entity);
        recalculateAllMemberLevels();
        return toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MembershipLevel entity = getEntity(id);
        if (membershipRepository.countByMemberLevel(entity.getLevelCode()) > 0) {
            throw new MembershipBadRequestException(
                    "仍有会员处于等级「" + entity.getLevelCode() + "」，无法删除");
        }
        if (entity.getMinPoints() == 0 && membershipLevelRepository.countByMinPoints(0) <= 1) {
            throw new MembershipBadRequestException("至少保留一条 minPoints 为 0 的默认等级");
        }
        membershipLevelRepository.delete(entity);
        recalculateAllMemberLevels();
    }

    @Override
    @Transactional(readOnly = true)
    public String resolveLevelCode(long points) {
        return membershipLevelRepository.findAllByOrderByMinPointsDesc().stream()
                .filter(level -> points >= level.getMinPoints())
                .map(MembershipLevel::getLevelCode)
                .findFirst()
                .orElse(DEFAULT_LEVEL_CODE);
    }

    @Override
    @Transactional(readOnly = true)
    public String resolveLevelName(String levelCode) {
        if (levelCode == null || levelCode.isBlank()) {
            return null;
        }
        return membershipLevelRepository.findByLevelCode(levelCode)
                .map(MembershipLevel::getLevelName)
                .orElse(levelCode);
    }

    @Override
    @Transactional
    public void recalculateAllMemberLevels() {
        List<Membership> memberships = membershipRepository.findAll();
        for (Membership membership : memberships) {
            String levelCode = resolveLevelCode(membership.getPoints());
            membership.setMemberLevel(levelCode);
        }
        membershipRepository.saveAll(memberships);
    }

    private MembershipLevel getEntity(Long id) {
        return membershipLevelRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException("会员等级不存在，id: " + id));
    }

    private void validateDto(MembershipLevelDto dto, Long excludeId) {
        if (dto.getLevelCode() == null || dto.getLevelCode().isBlank()) {
            throw new MembershipBadRequestException("等级代码不能为空");
        }
        if (dto.getLevelName() == null || dto.getLevelName().isBlank()) {
            throw new MembershipBadRequestException("等级名称不能为空");
        }
        if (dto.getMinPoints() < 0) {
            throw new MembershipBadRequestException("最低积分不能为负数");
        }
        String code = dto.getLevelCode().trim().toUpperCase();
        boolean duplicate = excludeId == null
                ? membershipLevelRepository.existsByLevelCode(code)
                : membershipLevelRepository.existsByLevelCodeAndIdNot(code, excludeId);
        if (duplicate) {
            throw new MembershipBadRequestException("等级代码已存在: " + code);
        }
    }

    private void applyDto(MembershipLevel entity, MembershipLevelDto dto) {
        entity.setLevelCode(dto.getLevelCode().trim().toUpperCase());
        entity.setLevelName(dto.getLevelName().trim());
        entity.setMinPoints(dto.getMinPoints());
        entity.setDescription(dto.getDescription());
        entity.setSortOrder(dto.getSortOrder() > 0 ? dto.getSortOrder() : (int) dto.getMinPoints());
    }

    private MembershipLevelDto toDto(MembershipLevel entity) {
        return new MembershipLevelDto(
                entity.getId(),
                entity.getLevelCode(),
                entity.getLevelName(),
                entity.getMinPoints(),
                entity.getDescription(),
                entity.getSortOrder());
    }
}
