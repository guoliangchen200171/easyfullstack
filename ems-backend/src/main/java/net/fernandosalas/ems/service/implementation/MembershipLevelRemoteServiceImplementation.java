package net.fernandosalas.ems.service.implementation;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.client.MembershipClient;
import net.fernandosalas.ems.dto.MembershipLevelDto;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.service.MembershipLevelRemoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MembershipLevelRemoteServiceImplementation implements MembershipLevelRemoteService {

    private final MembershipClient membershipClient;

    @Override
    public List<MembershipLevelDto> findAll() {
        try {
            return membershipClient.listMembershipLevels().stream()
                    .map(this::toDto)
                    .toList();
        } catch (FeignException ex) {
            log.error("Failed to list membership levels", ex);
            throw new InvalidSearchParameterException("会员等级服务暂不可用，请稍后重试");
        }
    }

    @Override
    public MembershipLevelDto findById(Long id) {
        try {
            return toDto(membershipClient.getMembershipLevel(id));
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("会员等级不存在，id: " + id);
        } catch (FeignException ex) {
            log.error("Failed to get membership level id={}", id, ex);
            throw new InvalidSearchParameterException(parseMessage(ex));
        }
    }

    @Override
    public MembershipLevelDto create(MembershipLevelDto dto) {
        try {
            return toDto(membershipClient.createMembershipLevel(toClientDto(dto)));
        } catch (FeignException ex) {
            log.error("Failed to create membership level", ex);
            throw new InvalidSearchParameterException(parseMessage(ex));
        }
    }

    @Override
    public MembershipLevelDto update(Long id, MembershipLevelDto dto) {
        try {
            return toDto(membershipClient.updateMembershipLevel(id, toClientDto(dto)));
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("会员等级不存在，id: " + id);
        } catch (FeignException ex) {
            log.error("Failed to update membership level id={}", id, ex);
            throw new InvalidSearchParameterException(parseMessage(ex));
        }
    }

    @Override
    public void delete(Long id) {
        try {
            membershipClient.deleteMembershipLevel(id);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("会员等级不存在，id: " + id);
        } catch (FeignException ex) {
            log.error("Failed to delete membership level id={}", id, ex);
            throw new InvalidSearchParameterException(parseMessage(ex));
        }
    }

    private String parseMessage(FeignException ex) {
        String body = ex.contentUTF8();
        if (body != null && body.contains("\"message\"")) {
            int start = body.indexOf("\"message\"");
            int colon = body.indexOf(':', start);
            int quoteStart = body.indexOf('"', colon + 1);
            int quoteEnd = body.indexOf('"', quoteStart + 1);
            if (quoteStart >= 0 && quoteEnd > quoteStart) {
                return body.substring(quoteStart + 1, quoteEnd);
            }
        }
        return "会员等级操作失败，请稍后重试";
    }

    private MembershipLevelDto toDto(net.fernandosalas.ems.client.dto.MembershipLevelDto client) {
        return new MembershipLevelDto(
                client.getId(),
                client.getLevelCode(),
                client.getLevelName(),
                client.getMinPoints(),
                client.getDescription(),
                client.getSortOrder());
    }

    private net.fernandosalas.ems.client.dto.MembershipLevelDto toClientDto(MembershipLevelDto dto) {
        return new net.fernandosalas.ems.client.dto.MembershipLevelDto(
                dto.getId(),
                dto.getLevelCode(),
                dto.getLevelName(),
                dto.getMinPoints(),
                dto.getDescription(),
                dto.getSortOrder());
    }
}
