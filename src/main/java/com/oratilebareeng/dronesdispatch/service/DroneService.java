package com.oratilebareeng.dronesdispatch.service;

import com.oratilebareeng.dronesdispatch.model.*;
import com.oratilebareeng.dronesdispatch.repository.DroneRepository;
import com.oratilebareeng.dronesdispatch.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;
    private Logger logger = LoggerFactory.getLogger(DroneService.class);
    final int minimumLoadBatteryCapacity = 25;

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

    // update drone
    @Transactional
    public void updateDrone(String serialNumber,
                            Integer batteryCapacity, DroneState droneState){
        // check if student exists
        Drone databaseDrone = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(()-> new IllegalStateException("Drone with Serial Number: "
                        + serialNumber + " does not exist"));

        if(batteryCapacity == null && droneState == null){
            throw new IllegalStateException("Please provide new details to update " + serialNumber);
        }

        // validate battery capacity is within allowed range 0-100
        // and is not the same as student name in db
        if(batteryCapacity != null && batteryCapacity != databaseDrone.getBatteryCapacity()){
            if(batteryCapacity < 0 || batteryCapacity > 100) {
                throw new IllegalStateException("Battery capacity must be between 0 and 100");
            }
            databaseDrone.setBatteryCapacity(batteryCapacity);
        }


        // vaidate drone state not null, not empty, not same
        if(droneState != null
                && !Objects.equals(droneState, databaseDrone.getState())
        ){
                databaseDrone.setState(droneState);
        }
    }

    // load drone with medication
    public String loadDroneWithMedication(Medication medication, String serialNumber){
        Optional<Drone> drone = droneRepository.findBySerialNumber(serialNumber);
        if(drone.isPresent()) {
            double availableSpace = drone.get().getWeight() - drone.get().getLoadedCapacity();
            if(availableSpace < 0) {
                availableSpace = 0;
            }
            // prevent load drone if medication weight is more than available capacity
            if (!(availableSpace >= medication.getWeight())) {
               return  "Not enough space in drone. Available space: " + availableSpace + "g";
            } else if (drone.get().getBatteryCapacity() < minimumLoadBatteryCapacity){
                // prevent load drone if battery capacity is below minimumLoadBatteryCapacity
                return "Medication not loaded. "
                        + serialNumber
                        + " battery level: "
                        + drone.get().getBatteryCapacity()
                        + " is below 25%"
                        + "\nPlease charge drone before loading";

            } else if(drone.get().getLoadedMedication().contains(medication)) {
                // ensure duplicate medication is not loaded
                return medication.getName()
                        + " with code "
                        + medication.getCode()
                        + " is already loaded onto "
                        + serialNumber;
            } else if(!drone.get().getState().equals(DroneState.IDLE)
            && !drone.get().getState().equals(DroneState.LOADING)) {
                // ensure only IDLE and LOADING state drones can load medication
                return serialNumber
                + " is currently "
                        + drone.get().getState();
            } else if(drone.get().getLoadedCapacity() == drone.get().getWeight()) {
                // update drone is fully loaded
                updateDrone(serialNumber,null , DroneState.LOADED);
            }
            else {
                drone.get().loadMedication(medication);
                // update drone state
                updateDrone(serialNumber, null, DroneState.LOADING);
                droneRepository.save(drone.get());
                return medication.getName() + " has been loaded onto " + serialNumber;
            }
        }
        return serialNumber + " does not exist";
    }

    // get medication loaded onto drone
    public List<Medication> getLoadedMedication(String droneSerialNumber){
        // ensure drone exists
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(droneSerialNumber);
        if(databaseDrone.isPresent()) {
            return databaseDrone.get().getLoadedMedication();
        } else {
            throw new IllegalStateException("Drone with Serial Number: " + droneSerialNumber+"  does not exist");
        }
    }

    // check available drones for loading
    public Page<Drone> getAvailableDrones(DronePage dronePage){
        // configure sort
        Sort dronesSort = Sort.by(dronePage.getSortDirection(), dronePage.getSortBy());
        // get paged list
        Pageable pageableDrones = PageRequest.of(dronePage.getPageNumber(), dronePage.getPageSize(), dronesSort);
        List<Drone> allDbDrones = droneRepository.findAll();
        if(!allDbDrones.isEmpty()){
            return new PageImpl<>(
                    allDbDrones.stream()
                    .filter(drone -> drone.getState()
                            .equals(DroneState.IDLE))
                    .collect(Collectors.toList()), pageableDrones, allDbDrones.size());
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    // get drone battery level
    public String getDroneBatteryLevel(String droneSerialNumber) {
        // ensure drone exists
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(droneSerialNumber);
        if(databaseDrone.isPresent()) {
            return databaseDrone.get().getSerialNumber()
            + " battery level is: " + databaseDrone.get().getBatteryCapacity() + "%";
        }else {
            return "Drone with Serial Number: " + droneSerialNumber+"  does not exist";
        }

    }

    // log drones battery level
    public void logBatteryLevel(){
        List<Drone> allDrones = droneRepository.findAll();
        for (Drone drone : allDrones
             ) {
            logger.info(drone.getSerialNumber()
                    + " battery level is: " + drone.getBatteryCapacity() + "%");
        }
    }

}
