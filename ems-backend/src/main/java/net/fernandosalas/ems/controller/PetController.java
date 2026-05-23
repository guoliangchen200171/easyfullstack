package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/pets")
@AllArgsConstructor
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet newPet = petService.createPet(pet);
        return new ResponseEntity<>(newPet, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long petId) {
        Pet pet = petService.getPetById(petId);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable("id") Long petId,
                                         @RequestBody Pet pet) {
        Pet updatedPet = petService.updatePet(petId, pet);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @PutMapping("{petId}/adopt/{studentId}")
    public ResponseEntity<Pet> adoptPet(@PathVariable("petId") Long petId,
                                        @PathVariable("studentId") Long studentId) {
        Pet adoptedPet = petService.adoptPet(petId, studentId);
        return new ResponseEntity<>(adoptedPet, HttpStatus.OK);
    }

    @PutMapping("{petId}/return")
    public ResponseEntity<Pet> returnPet(@PathVariable("petId") Long petId) {
        Pet returnedPet = petService.returnPet(petId);
        return new ResponseEntity<>(returnedPet, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePet(@PathVariable("id") Long petId) {
        petService.deletePet(petId);
        return new ResponseEntity<>("Delete Pet Successfully", HttpStatus.OK);
    }
}
