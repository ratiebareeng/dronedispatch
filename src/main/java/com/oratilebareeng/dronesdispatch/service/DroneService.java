package com.oratilebareeng.dronesdispatch.service;

import com.oratilebareeng.dronesdispatch.model.Drone;
import com.oratilebareeng.dronesdispatch.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;

    // register drone
    public Drone registerDrone(Drone drone) {
        // ensure drone is not already registered
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(drone.getSerialNumber());
        if(databaseDrone.isPresent()) {
            throw new IllegalStateException("Drone with Serial Number: " + drone.getSerialNumber()+ " exists. Serial Number must be unique.");
        } else {
            try {
                droneRepository.save(drone);
                return drone;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        }

    }


}
