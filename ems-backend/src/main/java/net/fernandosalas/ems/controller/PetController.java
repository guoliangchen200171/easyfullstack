package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@AllArgsConstructor
@Tag(name = "宠物管理", description = "宠物 CRUD、领养、归还与名称搜索")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "创建宠物", description = "需要 ADMIN 角色")
    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet newPet = petService.createPet(pet);
        return new ResponseEntity<>(newPet, HttpStatus.CREATED);
    }

    @Operation(summary = "按名称搜索宠物", description = "需要 ADMIN 或 STUDENT 角色，名称子串匹配")
    @GetMapping("/search")
    public ResponseEntity<List<Pet>> searchPetsByName(
            @Parameter(description = "宠物名称关键词", required = true) @RequestParam("name") String name) {
        List<Pet> pets = petService.searchPetsByName(name);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @Operation(summary = "按 ID 查询宠物", description = "需要 ADMIN 角色")
    @GetMapping("{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long petId) {
        Pet pet = petService.getPetById(petId);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @Operation(summary = "查询宠物列表", description = "需要 ADMIN 或 STUDENT 角色")
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @Operation(summary = "更新宠物", description = "需要 ADMIN 角色")
    @PutMapping("{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable("id") Long petId,
                                         @RequestBody Pet pet) {
        Pet updatedPet = petService.updatePet(petId, pet);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @Operation(summary = "管理员指定学生领养", description = "需要 ADMIN 角色，直接完成领养（非审批流）")
    @PutMapping("{petId}/adopt/{studentId}")
    public ResponseEntity<Pet> adoptPet(@PathVariable("petId") Long petId,
                                        @PathVariable("studentId") Long studentId) {
        Pet adoptedPet = petService.adoptPet(petId, studentId);
        return new ResponseEntity<>(adoptedPet, HttpStatus.OK);
    }

    @Operation(summary = "归还宠物", description = "需要 ADMIN 角色")
    @PutMapping("{petId}/return")
    public ResponseEntity<Pet> returnPet(@PathVariable("petId") Long petId) {
        Pet returnedPet = petService.returnPet(petId, true);
        return new ResponseEntity<>(returnedPet, HttpStatus.OK);
    }

    @Operation(summary = "删除宠物", description = "需要 ADMIN 角色")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePet(@PathVariable("id") Long petId) {
        petService.deletePet(petId);
        return new ResponseEntity<>("Delete Pet Successfully", HttpStatus.OK);
    }
}
