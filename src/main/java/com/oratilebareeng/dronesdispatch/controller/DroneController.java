package com.oratilebareeng.dronesdispatch.controller;

import com.oratilebareeng.dronesdispatch.model.*;
import com.oratilebareeng.dronesdispatch.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   public ResponseEntity<List<Medication>> getLoadedMedication(@PathVariable String serialNumber){
       return new ResponseEntity<>(
               droneService.getLoadedMedication(serialNumber),
               HttpStatus.OK
       );
   }

    // register drone
    @PostMapping("/registerDrone")
    public ResponseEntity<String> registerDrone(@RequestBody Drone drone) {
       try {
           Drone registeredDrone = droneService.registerDrone(drone);
           return new ResponseEntity<>(
                   registeredDrone.getSerialNumber() + " has been registered",
                   HttpStatus.OK
           );
       } catch (IllegalStateException e){
           return new ResponseEntity<>(
                   drone.getSerialNumber()
                   + " not registered. " + e.getMessage(),
                   HttpStatus.BAD_REQUEST
           );
       }
    }

    // delete drone by serial number
    @DeleteMapping(path = "/deleteDrone/{serialNumber}")
    public ResponseEntity<String> deleteDrone(@PathVariable("serialNumber") String serialNumber){
        try {
            Drone deletedDrone = droneService.deleteDrone(serialNumber);
            return new ResponseEntity<>(
                    serialNumber + " has been deleted",
                    HttpStatus.OK
            );
        } catch (IllegalStateException e){
            return new ResponseEntity<>(
                    serialNumber
                            + " not deleted. " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // update drone
    @PutMapping(path = "updateDrone/{serialNumber}")
    public ResponseEntity<String> updateDrone(
            @PathVariable("serialNumber") String serialNumber,
            @RequestParam(required = false) Integer batteryCapacity,
            @RequestParam(required = false) String droneState){

        try {
            droneService.updateDrone(serialNumber, batteryCapacity, DroneState.valueOf(droneState));
            return new ResponseEntity<>(
                    serialNumber + " updated.",
                    HttpStatus.OK
            );
        } catch (IllegalStateException e){
            return new ResponseEntity<>(
                    serialNumber
                            + " not updated. " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // load drone with medication
    @PostMapping("/loadDrone/{serialNumber}")
    public ResponseEntity<String> loadDroneWithMedication(
            @PathVariable("serialNumber") String serialNumber,
            @RequestBody Medication medication) {
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



}
