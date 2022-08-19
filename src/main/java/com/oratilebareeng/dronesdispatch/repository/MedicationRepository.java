package com.oratilebareeng.dronesdispatch.repository;

import com.oratilebareeng.dronesdispatch.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
