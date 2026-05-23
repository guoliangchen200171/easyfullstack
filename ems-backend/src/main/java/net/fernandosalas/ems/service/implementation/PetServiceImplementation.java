package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.StudentAlreadyHasPetException;
import net.fernandosalas.ems.repository.PetRepository;
import net.fernandosalas.ems.repository.StudentRepository;
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

    @Override
    public Pet createPet(Pet pet) {
        pet.setAdopted(false);
        pet.setStudent(null);
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
                    throw new StudentAlreadyHasPetException("Student already has a pet");
                }
            });

            existingPet.setStudent(student);
            existingPet.setAdopted(true);
        } else {
            existingPet.setStudent(null);
            existingPet.setAdopted(false);
        }

        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        petRepository.deleteById(petId);
    }
}
