package com.oratilebareeng.dronesdispatch.controller;

import com.oratilebareeng.dronesdispatch.model.Medication;
import com.oratilebareeng.dronesdispatch.model.MedicationPage;
import com.oratilebareeng.dronesdispatch.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/drones-dispatch")
@RequiredArgsConstructor
public class MedicationController {

    private MedicationService medicationService;

    // get paged list of all medication
    @GetMapping(path = "/medication")
    public ResponseEntity<Page<Medication>> getMedication(MedicationPage medicationPage){
        return new ResponseEntity<>(
                medicationService.getAllMedication(medicationPage),
                HttpStatus.OK
        );
    }

    // save medication to database
    @PostMapping("/saveMedication")
    public ResponseEntity<String> saveMedication(@Valid @RequestBody Medication medication) {
        return new ResponseEntity<>(
                medicationService.saveMedication(medication),
                HttpStatus.OK
        );
    }

    // delete medication record
    @DeleteMapping(path = "/deleteMedication/{code}")
    public ResponseEntity<String> deleteMedication(@PathVariable("serialNumber") String code){
        return new ResponseEntity<>(
                medicationService.deleteMedication(code),
                HttpStatus.OK
        );
    }
}
