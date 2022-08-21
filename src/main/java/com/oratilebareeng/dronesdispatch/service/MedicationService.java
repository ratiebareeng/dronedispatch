package com.oratilebareeng.dronesdispatch.service;

import com.oratilebareeng.dronesdispatch.model.Medication;
import com.oratilebareeng.dronesdispatch.model.MedicationPage;
import com.oratilebareeng.dronesdispatch.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;

    // save new medication to database
    public String saveMedication(Medication medication){
        Optional<Medication> databaseMedication = medicationRepository.findByCode(medication.getCode());
        if(databaseMedication.isPresent()){
            throw new IllegalStateException("Medication with code: "
                    + medication.getCode()
            + " already exists.\nMedication code should be unique.");
        } else {
            medicationRepository.save(medication);
            return medication.getCode()
            + " has been saved.";
        }
    }

    // get medication by code
    public Medication getMedicationByCode(String code) {
        return medicationRepository.findByCode(code)
                .orElseThrow(()-> new IllegalStateException(code + " does not exist"));
    }

    // get list of medication
    public Page<Medication> getAllMedication(MedicationPage medicationPage) {
        // configure sort
        Sort medicationSort = Sort.by(medicationPage.getSortDirection(), medicationPage.getSortBy());
        // get paged list
        Pageable pageableDrones = PageRequest.of(medicationPage.getPageNumber(), medicationPage.getPageSize(), medicationSort);
        List<Medication> allDbMedication = medicationRepository.findAll();
        if(!allDbMedication.isEmpty()){
            // return paged list of all medication
            return new PageImpl<>(
                    allDbMedication, pageableDrones, allDbMedication.size());
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    // delete medication record
    public String deleteMedication(String code) {
        Optional<Medication> databaseMedication = medicationRepository.findByCode(code);
        if (!databaseMedication.isPresent()) {
            throw new IllegalStateException("Medication with code: "
                    + code
                    + " does not exist in the database.");
        } else {
            medicationRepository.delete(databaseMedication.get());
            return code + " deleted.";
        }
    }

}
