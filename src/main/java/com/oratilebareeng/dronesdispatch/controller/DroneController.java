package com.oratilebareeng.dronesdispatch.controller;

import com.oratilebareeng.dronesdispatch.model.Drone;
import com.oratilebareeng.dronesdispatch.model.DronePage;
import com.oratilebareeng.dronesdispatch.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    "Drone: " + serialNumber + " has been deleted",
                    HttpStatus.OK
            );
        } catch (IllegalStateException e){
            return new ResponseEntity<>(
                    "Drone: " + serialNumber
                            + " not deleted. " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }



}
