package com.oratilebareeng.dronesdispatch.service;

import com.oratilebareeng.dronesdispatch.exception.ObjectExistsException;
import com.oratilebareeng.dronesdispatch.exception.ObjectNotFoundException;
import com.oratilebareeng.dronesdispatch.exception.ValidationException;
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
    private final MedicationRepository medicationRepository;
    private final Logger logger = LoggerFactory.getLogger(DroneService.class);
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
    public String registerDrone(Drone drone) {
        // ensure drone is not already registered
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(drone.getSerialNumber());
        if(databaseDrone.isPresent()) {
            // make sure drone serial number is unique
            throw new ObjectExistsException("Drone with Serial Number: " + drone.getSerialNumber()+ " exists.\nSerial Number must be unique.");
        } else {
            droneRepository.save(drone);
            return drone.getSerialNumber() + " has been registered";
        }

    }

    // delete drone
    public String deleteDrone(String serialNumber) {
        // ensure drone is already registered
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(serialNumber);
        if(!databaseDrone.isPresent()) {
            throw new ObjectNotFoundException("Drone with Serial Number: " + serialNumber + " does not exist.");
        } else {
            droneRepository.delete(databaseDrone.get());
            return databaseDrone.get().getSerialNumber() + " has been deleted";
        }
    }

    // update drone
    @Transactional
    public void updateDrone(String serialNumber,
                            Integer batteryCapacity, DroneState droneState) throws ObjectNotFoundException {
        // check if student exists
        Drone databaseDrone = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(()-> new ObjectNotFoundException("Drone with Serial Number: "
                        + serialNumber + " does not exist"));

        if(batteryCapacity == null && droneState == null){
            throw new ValidationException("Please provide new details to update " + serialNumber);
        }

        // validate battery capacity is within allowed range 0-100
        // and is not the same as student name in db
        if(batteryCapacity != null && batteryCapacity != databaseDrone.getBatteryCapacity()){
            if(batteryCapacity < 0 || batteryCapacity > 100) {
                throw new ValidationException("Battery capacity must be between 0 and 100");
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
    public String loadDroneWithMedication(Medication medication, String serialNumber) throws ObjectNotFoundException {
        Optional<Drone> drone = droneRepository.findBySerialNumber(serialNumber);
        if(drone.isPresent()) {
            double availableSpace = drone.get().getWeight() - drone.get().getLoadedCapacity();
            if(availableSpace < 0) {
                availableSpace = 0;
            }
            // prevent load drone if medication weight is more than available capacity
            if (availableSpace < medication.getWeight() || drone.get().getWeight() < medication.getWeight()) {
               throw new ValidationException("Not enough space in drone.\nAvailable space: " + availableSpace + "g");
            } else if (drone.get().getBatteryCapacity() < minimumLoadBatteryCapacity){
                // prevent load drone if battery capacity is below minimumLoadBatteryCapacity
                throw new ValidationException("Medication not loaded because "
                        + serialNumber
                        + " battery level: "
                        + drone.get().getBatteryCapacity()
                        + "% is below 25%"
                        + "\nPlease charge drone before loading");

            } else if(drone.get().getLoadedMedication().contains(medication)
                    || drone.get().getLoadedMedication().stream().anyMatch(
                    med -> med.getCode().equals(medication.getCode())
            )) {
                // ensure duplicate medication is not loaded
                throw new ObjectExistsException(medication.getName()
                        + " with code "
                        + medication.getCode()
                        + " is already loaded onto "
                        + serialNumber);
            } else if(!drone.get().getState().equals(DroneState.IDLE)
            && !drone.get().getState().equals(DroneState.LOADING)) {
                // ensure only IDLE and LOADING state drones can load medication
                throw new ValidationException(serialNumber
                        + " is currently "
                        + drone.get().getState());
            } else {
                drone.get().loadMedication(medication);
                // update drone state
                updateDrone(serialNumber, null, DroneState.LOADING);

                // update drone is fully loaded
                if(drone.get().getLoadedCapacity() == drone.get().getWeight()) {
                    updateDrone(serialNumber,null , DroneState.LOADED);
                }
                droneRepository.save(drone.get());
                return medication.getName() + " has been loaded onto " + serialNumber;
            }
        }
        return serialNumber + " does not exist";
    }

    // get medication loaded onto drone
    public List<Medication> getLoadedMedication(String droneSerialNumber) throws ObjectNotFoundException {
        // ensure drone exists
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(droneSerialNumber);
        if(databaseDrone.isPresent()) {
            return databaseDrone.get().getLoadedMedication();
        } else {
            throw new ObjectNotFoundException("Drone with Serial Number: " + droneSerialNumber+"  does not exist");
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
            // filter get only idle drones
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
    public String getDroneBatteryLevel(String droneSerialNumber) throws ObjectNotFoundException {
        // ensure drone exists
        Optional<Drone> databaseDrone = droneRepository.findBySerialNumber(droneSerialNumber);
        if(databaseDrone.isPresent()) {
            return databaseDrone.get().getSerialNumber()
            + " battery level is: " + databaseDrone.get().getBatteryCapacity() + "%";
        }else {
            throw new ObjectNotFoundException( "Drone with Serial Number: " + droneSerialNumber+"  does not exist");
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
