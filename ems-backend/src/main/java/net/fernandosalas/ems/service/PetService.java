package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.Pet;

import java.util.List;

public interface PetService {

    Pet createPet(Pet pet);

    Pet getPetById(Long petId);

    List<Pet> getAllPets();

    Pet updatePet(Long petId, Pet pet);

    Pet adoptPet(Long petId, Long studentId);

    void deletePet(Long petId);
}
