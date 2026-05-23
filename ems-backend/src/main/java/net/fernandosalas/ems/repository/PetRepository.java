package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
