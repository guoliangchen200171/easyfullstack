package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.PetRepository;
import net.fernandosalas.ems.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImplementation implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public Pet createPet(Pet pet) {
        pet.setAdopted(false);
        return petRepository.save(pet);
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public Pet updatePet(Long petId, Pet pet) {
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        existingPet.setName(pet.getName());
        existingPet.setDescription(pet.getDescription());
        existingPet.setAge(pet.getAge());
        existingPet.setCategory(pet.getCategory());
        existingPet.setAdopted(pet.isAdopted());
        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet was not found with id: " + petId));
        petRepository.deleteById(petId);
    }
}
