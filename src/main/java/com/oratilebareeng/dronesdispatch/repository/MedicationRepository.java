package com.oratilebareeng.dronesdispatch.repository;

import com.oratilebareeng.dronesdispatch.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByCode(String code);
}
