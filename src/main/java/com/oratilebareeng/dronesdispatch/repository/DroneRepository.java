package com.oratilebareeng.dronesdispatch.repository;

import com.oratilebareeng.dronesdispatch.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    // find drone by serial number
    @Query("SELECT d FROM Drone d WHERE d.serialNumber = ?1")
    Optional<Drone> findBySerialNumber(String serialNumber);
}
