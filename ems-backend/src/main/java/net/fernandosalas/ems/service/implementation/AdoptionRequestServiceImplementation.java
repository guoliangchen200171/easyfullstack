package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.entity.AdoptionRequest;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;
import net.fernandosalas.ems.exception.InvalidAdoptionRequestStateException;
import net.fernandosalas.ems.exception.PendingAdoptionRequestExistsException;
import net.fernandosalas.ems.exception.PetAlreadyAdoptedException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.StudentAlreadyHasPetException;
import net.fernandosalas.ems.exception.StudentBannedFromAdoptionException;
import net.fernandosalas.ems.mapper.AdoptionRequestMapper;
import net.fernandosalas.ems.repository.AdoptionRequestRepository;
import net.fernandosalas.ems.repository.PetRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.service.AdoptionRequestService;
import net.fernandosalas.ems.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdoptionRequestServiceImplementation implements AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;
    private final StudentRepository studentRepository;
    private final PetRepository petRepository;
    private final PetService petService;

    @Override
    @Transactional
    public AdoptionRequestDto applyForAdoption(Long studentId, Long petId) {
        validateStudentCanApply(studentId);
        if (adoptionRequestRepository.existsByStudentIdAndStatus(studentId, AdoptionRequestStatus.PENDING)) {
            throw new PendingAdoptionRequestExistsException("您已有待审批的领养申请");
        }

        Pet pet = petRepository.findByIdWithStudent(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        if (pet.isAdopted() || pet.getStudent() != null) {
            throw new PetAlreadyAdoptedException("宠物已经被领养");
        }

        Student student = studentRepository.findByIdWithDetails(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found with id: " + studentId));

        AdoptionRequest request = new AdoptionRequest();
        request.setStudent(student);
        request.setPet(pet);
        request.setStatus(AdoptionRequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());
        AdoptionRequest saved = adoptionRequestRepository.save(request);
        return AdoptionRequestMapper.mapToDto(
                adoptionRequestRepository.findWithDetailsById(saved.getId()).orElseThrow());
    }

    @Override
    public PageResponse<AdoptionRequestDto> getRequestsPage(int page, int size, AdoptionRequestStatus status) {
        Page<AdoptionRequest> requestPage = status != null
                ? adoptionRequestRepository.findByStatusOrderByRequestedAtDesc(
                        status, PageRequest.of(page, size))
                : adoptionRequestRepository.findAllByOrderByRequestedAtDesc(PageRequest.of(page, size));
        List<AdoptionRequestDto> content = requestPage.getContent().stream()
                .map(AdoptionRequestMapper::mapToDto)
                .collect(Collectors.toList());
        return PageResponse.from(requestPage, content);
    }

    @Override
    public AdoptionRequestDto getMyPendingRequest(Long studentId) {
        return adoptionRequestRepository
                .findByStudentIdAndStatus(studentId, AdoptionRequestStatus.PENDING)
                .map(AdoptionRequestMapper::mapToDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public AdoptionRequestDto approve(Long requestId) {
        AdoptionRequest request = getPendingRequest(requestId);
        petService.adoptPet(request.getPet().getId(), request.getStudent().getId());
        request.setStatus(AdoptionRequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        adoptionRequestRepository.save(request);
        return AdoptionRequestMapper.mapToDto(
                adoptionRequestRepository.findWithDetailsById(requestId).orElseThrow());
    }

    @Override
    @Transactional
    public AdoptionRequestDto deny(Long requestId) {
        AdoptionRequest request = getPendingRequest(requestId);
        request.setStatus(AdoptionRequestStatus.DENIED);
        request.setReviewedAt(LocalDateTime.now());
        adoptionRequestRepository.save(request);
        return AdoptionRequestMapper.mapToDto(
                adoptionRequestRepository.findWithDetailsById(requestId).orElseThrow());
    }

    private AdoptionRequest getPendingRequest(Long requestId) {
        AdoptionRequest request = adoptionRequestRepository.findWithDetailsById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Adoption request was not found with id: " + requestId));
        if (request.getStatus() != AdoptionRequestStatus.PENDING) {
            throw new InvalidAdoptionRequestStateException("该申请已处理，无法重复操作");
        }
        return request;
    }

    private void validateStudentCanApply(Long studentId) {
        Student student = studentRepository.findByIdWithDetails(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found with id: " + studentId));
        if (student.getPet() != null) {
            throw new StudentAlreadyHasPetException("最多只能有一只宠物");
        }
        if (student.getReturnCount() >= 3) {
            throw new StudentBannedFromAdoptionException("你退还次数过多被禁止领养");
        }
    }
}
