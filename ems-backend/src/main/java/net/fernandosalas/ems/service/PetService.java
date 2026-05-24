package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.Pet;

import java.util.List;

public interface PetService {

    Pet createPet(Pet pet);

    Pet getPetById(Long petId);

    List<Pet> getAllPets();

    List<Pet> searchPetsByName(String name);

    Pet updatePet(Long petId, Pet pet);

    Pet adoptPet(Long petId, Long studentId);

    Pet returnPet(Long petId, boolean incrementStudentReturnCount);

    void deletePet(Long petId);
}
