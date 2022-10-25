package com.oratilebareeng.dronesdispatch.controller;

import com.oratilebareeng.dronesdispatch.exception.ObjectNotFoundException;
import com.oratilebareeng.dronesdispatch.model.*;
import com.oratilebareeng.dronesdispatch.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/drones-dispatch")
@RequiredArgsConstructor
public class DroneController {
    private final DroneService droneService;

   @GetMapping(path = "/drones")
   public ResponseEntity<Page<Drone>> getDrones(DronePage dronePage){
       return new ResponseEntity<>(
               droneService.getDrones(dronePage),
               HttpStatus.OK
       );
   }

   // get loaded medication for drone
   @GetMapping(path = "/loadedMedication/{serialNumber}")
   public ResponseEntity<List<Medication>> getLoadedMedication(@PathVariable String serialNumber) throws ObjectNotFoundException {
       return new ResponseEntity<>(
               droneService.getLoadedMedication(serialNumber),
               HttpStatus.OK
       );
   }

    // register drone
    @PostMapping("/registerDrone")
    public ResponseEntity<String> registerDrone(@Valid @RequestBody Drone drone) {
           return new ResponseEntity<>(
                   droneService.registerDrone(drone),
                   HttpStatus.OK
           );

    }

    // delete drone by serial number
    @DeleteMapping(path = "/deleteDrone/{serialNumber}")
    public ResponseEntity<String> deleteDrone(@PathVariable("serialNumber") String serialNumber) throws ObjectNotFoundException {
            return new ResponseEntity<>(
                    droneService.deleteDrone(serialNumber),
                    HttpStatus.OK
            );

    }

    // update drone
    @PutMapping(path = "updateDrone/{serialNumber}")
    public ResponseEntity<String> updateDrone(
            @PathVariable("serialNumber") String serialNumber,
            @RequestParam(required = false) Integer batteryCapacity,
            @RequestParam(required = false) String droneState) throws ObjectNotFoundException {
            droneService.updateDrone(serialNumber, batteryCapacity, DroneState.valueOf(droneState));
            return new ResponseEntity<>(
                    serialNumber + " updated.",
                    HttpStatus.OK
            );

    }

    // load drone with medication
    @PostMapping("/loadDrone/{serialNumber}")
    public ResponseEntity<String> loadDroneWithMedication(
            @PathVariable("serialNumber") String serialNumber,
           @Valid @RequestBody Medication medication) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                droneService.loadDroneWithMedication(medication, serialNumber),
                HttpStatus.OK
        );
    }

    // check available drones
    @GetMapping(path = "/availableDrones")
    public ResponseEntity<Page<Drone>> getAvailableDrones(DronePage dronePage){
        return new ResponseEntity<>(
                droneService.getAvailableDrones(dronePage),
                HttpStatus.OK
        );
    }

    // check drone battery level
    @GetMapping(path = "/batteryLevel/{serialNumber}")
    public ResponseEntity<String> getDroneBatteryLevel(
            @PathVariable("serialNumber") String serialNumber
    ) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                droneService.getDroneBatteryLevel(serialNumber),
                HttpStatus.OK
        );
    }

}
