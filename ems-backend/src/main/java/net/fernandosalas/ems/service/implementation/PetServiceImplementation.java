package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.exception.PetAlreadyAdoptedException;
import net.fernandosalas.ems.exception.PetNotAdoptedException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.StudentAlreadyHasPetException;
import net.fernandosalas.ems.exception.StudentBannedFromAdoptionException;
import net.fernandosalas.ems.repository.PetRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import net.fernandosalas.ems.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImplementation implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

    @Override
    public Pet createPet(Pet pet) {
        pet.setAdopted(false);
        pet.setStudent(null);
        pet.setAdoptionCount(0);
        pet.setReturnCount(0);
        return petRepository.save(pet);
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findByIdWithStudent(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public Pet updatePet(Long petId, Pet pet) {
        Pet existingPet = petRepository.findByIdWithStudent(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        existingPet.setName(pet.getName());
        existingPet.setDescription(pet.getDescription());
        existingPet.setAge(pet.getAge());
        existingPet.setCategory(pet.getCategory());

        if (pet.isAdopted()) {
            Long studentId = pet.getStudentId();
            if (studentId == null) {
                throw new ResourceNotFoundException("Student id is required when pet is adopted");
            }
            Student student = studentRepository.findByIdWithDetails(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student was not found with id: " + studentId));

            petRepository.findByStudentId(studentId).ifPresent(existingStudentPet -> {
                if (!existingStudentPet.getId().equals(petId)) {
                    throw new StudentAlreadyHasPetException("最多只能有一只宠物");
                }
            });

            if (student.getReturnCount() >= 3) {
                throw new StudentBannedFromAdoptionException("你退还次数过多被禁止领养");
            }

            existingPet.setStudent(student);
            existingPet.setAdopted(true);
        } else {
            existingPet.setStudent(null);
            existingPet.setAdopted(false);
        }

        return petRepository.save(existingPet);
    }

    @Override
    public Pet adoptPet(Long petId, Long studentId) {
        Pet pet = petRepository.findByIdWithStudent(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));

        if (pet.isAdopted() || pet.getStudent() != null) {
            throw new PetAlreadyAdoptedException("宠物已经被领养");
        }

        Student student = studentRepository.findByIdWithDetails(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found with id: " + studentId));

        if (student.getPet() != null) {
            throw new StudentAlreadyHasPetException("最多只能有一只宠物");
        }

        if (student.getReturnCount() >= 3) {
            throw new StudentBannedFromAdoptionException("你退还次数过多被禁止领养");
        }

        pet.setStudent(student);
        pet.setAdopted(true);
        pet.setAdoptionCount(pet.getAdoptionCount() + 1);
        Pet savedPet = petRepository.save(pet);
        adoptionHistoryService.recordAdoption(student, savedPet);
        return savedPet;
    }

    @Override
    public Pet returnPet(Long petId, boolean incrementStudentReturnCount) {
        Pet pet = petRepository.findByIdWithStudent(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));

        if (!pet.isAdopted() || pet.getStudent() == null) {
            throw new PetNotAdoptedException("该宠物未被领养，无法归还");
        }

        Student student = pet.getStudent();
        Long studentId = student.getId();
        pet.setStudent(null);
        pet.setAdopted(false);
        pet.setReturnCount(pet.getReturnCount() + 1);
        Pet savedPet = petRepository.save(pet);

        if (incrementStudentReturnCount) {
            student.setReturnCount(student.getReturnCount() + 1);
            studentRepository.save(student);
        }

        adoptionHistoryService.recordReturn(studentId);
        return savedPet;
    }

    @Override
    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        petRepository.deleteById(petId);
    }
}
