package com.oratilebareeng.dronesdispatch.service;

import com.oratilebareeng.dronesdispatch.model.Drone;
import com.oratilebareeng.dronesdispatch.model.DronePage;
import com.oratilebareeng.dronesdispatch.model.DroneState;
import com.oratilebareeng.dronesdispatch.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;

    // list all drones
    public Page<Drone> getDrones(DronePage dronePage) {
        // configure sort
        Sort dronesSort = Sort.by(dronePage.getSortDirection(), dronePage.getSortBy());
        // get paged list
        Pageable pageableDrones = PageRequest.of(dronePage.getPageNumber(), dronePage.getPageSize(), dronesSort);
        return droneRepository.findAll(pageableDrones);
    }

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

    // delete drone
    public Drone deleteDrone(String serialNumber) {
        // ensure drone is already registered
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(serialNumber);
        if(!databaseDrone.isPresent()) {
            throw new IllegalStateException("Drone with Serial Number: " + serialNumber + " does not exist.");
        } else {
            try {
                droneRepository.delete(databaseDrone.get());
                return databaseDrone.get();
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }


}
